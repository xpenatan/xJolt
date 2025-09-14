package jolt.gdx;

import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodeAnimation;
import com.badlogic.gdx.graphics.g3d.model.NodeKeyframe;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Pool;

public class ModelInstanceDebug implements RenderableProvider, Pool.Poolable {
    public static boolean defaultShareKeyframes = true;

    public final Array<Material> materials = new Array();
    public final Array<Node> nodes = new Array();
    public final Array<Animation> animations = new Array();
    public Model model;
    public final Matrix4 transform = new Matrix4();
    public Object userData;

    public void copyNodes (Array<Node> nodes) {
        for (int i = 0, n = nodes.size; i < n; ++i) {
            final Node node = nodes.get(i);
            this.nodes.add(node.copy());
        }
        invalidate();
    }

    public void copyNodes (Array<Node> nodes, final String... nodeIds) {
        for (int i = 0, n = nodes.size; i < n; ++i) {
            final Node node = nodes.get(i);
            for (final String nodeId : nodeIds) {
                if (nodeId.equals(node.id)) {
                    this.nodes.add(node.copy());
                    break;
                }
            }
        }
        invalidate();
    }

    private void copyNodes (Array<Node> nodes, final Array<String> nodeIds) {
        for (int i = 0, n = nodes.size; i < n; ++i) {
            final Node node = nodes.get(i);
            for (final String nodeId : nodeIds) {
                if (nodeId.equals(node.id)) {
                    this.nodes.add(node.copy());
                    break;
                }
            }
        }
        invalidate();
    }

    /** Makes sure that each {@link NodePart} of the {@link Node} and its sub-nodes, doesn't reference a node outside this node
     * tree and that all materials are listed in the {@link #materials} array. */
    private void invalidate (Node node) {
        for (int i = 0, n = node.parts.size; i < n; ++i) {
            NodePart part = node.parts.get(i);
            ArrayMap<Node, Matrix4> bindPose = part.invBoneBindTransforms;
            if (bindPose != null) {
                for (int j = 0; j < bindPose.size; ++j) {
                    bindPose.keys[j] = getNode(bindPose.keys[j].id);
                }
            }
            if (!materials.contains(part.material, true)) {
                final int midx = materials.indexOf(part.material, false);
                if (midx < 0)
                    materials.add(part.material = part.material.copy());
                else
                    part.material = materials.get(midx);
            }
        }
        for (int i = 0, n = node.getChildCount(); i < n; ++i) {
            invalidate(node.getChild(i));
        }
    }

    private void invalidate () {
        for (int i = 0, n = nodes.size; i < n; ++i) {
            invalidate(nodes.get(i));
        }
    }

    public void copyAnimations (final Iterable<Animation> source) {
        for (final Animation anim : source) {
            copyAnimation(anim, defaultShareKeyframes);
        }
    }

    public void copyAnimations (final Iterable<Animation> source, boolean shareKeyframes) {
        for (final Animation anim : source) {
            copyAnimation(anim, shareKeyframes);
        }
    }

    public void copyAnimation (Animation sourceAnim) {
        copyAnimation(sourceAnim, defaultShareKeyframes);
    }

    public void copyAnimation (Animation sourceAnim, boolean shareKeyframes) {
        Animation animation = new Animation();
        animation.id = sourceAnim.id;
        animation.duration = sourceAnim.duration;
        for (final NodeAnimation nanim : sourceAnim.nodeAnimations) {
            final Node node = getNode(nanim.node.id);
            if (node == null) continue;
            NodeAnimation nodeAnim = new NodeAnimation();
            nodeAnim.node = node;
            if (shareKeyframes) {
                nodeAnim.translation = nanim.translation;
                nodeAnim.rotation = nanim.rotation;
                nodeAnim.scaling = nanim.scaling;
            } else {
                if (nanim.translation != null) {
                    nodeAnim.translation = new Array<NodeKeyframe<Vector3>>();
                    for (final NodeKeyframe<Vector3> kf : nanim.translation)
                        nodeAnim.translation.add(new NodeKeyframe<Vector3>(kf.keytime, kf.value));
                }
                if (nanim.rotation != null) {
                    nodeAnim.rotation = new Array<NodeKeyframe<Quaternion>>();
                    for (final NodeKeyframe<Quaternion> kf : nanim.rotation)
                        nodeAnim.rotation.add(new NodeKeyframe<Quaternion>(kf.keytime, kf.value));
                }
                if (nanim.scaling != null) {
                    nodeAnim.scaling = new Array<NodeKeyframe<Vector3>>();
                    for (final NodeKeyframe<Vector3> kf : nanim.scaling)
                        nodeAnim.scaling.add(new NodeKeyframe<Vector3>(kf.keytime, kf.value));
                }
            }
            if (nodeAnim.translation != null || nodeAnim.rotation != null || nodeAnim.scaling != null)
                animation.nodeAnimations.add(nodeAnim);
        }
        if (animation.nodeAnimations.size > 0) animations.add(animation);
    }

    public void getRenderables (Array<Renderable> renderables, Pool<Renderable> pool) {
        for (Node node : nodes) {
            getRenderables(node, renderables, pool);
        }
    }

    public Renderable getRenderable (final Renderable out) {
        return getRenderable(out, nodes.get(0));
    }

    public Renderable getRenderable (final Renderable out, final Node node) {
        return getRenderable(out, node, node.parts.get(0));
    }

    public Renderable getRenderable (final Renderable out, final Node node, final NodePart nodePart) {
        nodePart.setRenderable(out);
        if (nodePart.bones == null && transform != null)
            out.worldTransform.set(transform).mul(node.globalTransform);
        else if (transform != null)
            out.worldTransform.set(transform);
        else
            out.worldTransform.idt();
        out.userData = userData;
        return out;
    }

    protected void getRenderables (Node node, Array<Renderable> renderables, Pool<Renderable> pool) {
        if (node.parts.size > 0) {
            for (NodePart nodePart : node.parts) {
                if (nodePart.enabled) renderables.add(getRenderable(pool.obtain(), node, nodePart));
            }
        }

        for (Node child : node.getChildren()) {
            getRenderables(child, renderables, pool);
        }
    }

    public void calculateTransforms () {
        final int n = nodes.size;
        for (int i = 0; i < n; i++) {
            nodes.get(i).calculateTransforms(true);
        }
        for (int i = 0; i < n; i++) {
            nodes.get(i).calculateBoneTransforms(true);
        }
    }

    public BoundingBox calculateBoundingBox (final BoundingBox out) {
        out.inf();
        return extendBoundingBox(out);
    }

    public BoundingBox extendBoundingBox (final BoundingBox out) {
        final int n = nodes.size;
        for (int i = 0; i < n; i++)
            nodes.get(i).extendBoundingBox(out);
        return out;
    }

    public Animation getAnimation (final String id) {
        return getAnimation(id, false);
    }

    public Animation getAnimation (final String id, boolean ignoreCase) {
        final int n = animations.size;
        Animation animation;
        if (ignoreCase) {
            for (int i = 0; i < n; i++)
                if ((animation = animations.get(i)).id.equalsIgnoreCase(id)) return animation;
        } else {
            for (int i = 0; i < n; i++)
                if ((animation = animations.get(i)).id.equals(id)) return animation;
        }
        return null;
    }

    public Material getMaterial (final String id) {
        return getMaterial(id, true);
    }

    public Material getMaterial (final String id, boolean ignoreCase) {
        final int n = materials.size;
        Material material;
        if (ignoreCase) {
            for (int i = 0; i < n; i++)
                if ((material = materials.get(i)).id.equalsIgnoreCase(id)) return material;
        } else {
            for (int i = 0; i < n; i++)
                if ((material = materials.get(i)).id.equals(id)) return material;
        }
        return null;
    }
    public Node getNode (final String id) {
        return getNode(id, true);
    }
    public Node getNode (final String id, boolean recursive) {
        return getNode(id, recursive, false);
    }

    public Node getNode (final String id, boolean recursive, boolean ignoreCase) {
        return Node.getNode(nodes, id, recursive, ignoreCase);
    }

    @Override
    public void reset() {
        materials.clear();
        nodes.clear();
        animations.clear();
        model = null;
        transform.idt();
        userData = null;
    }
}