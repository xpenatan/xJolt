package jolt;

import com.github.xpenatan.jparser.loader.JParserLibraryLoader;
import com.github.xpenatan.jparser.loader.JParserLibraryLoaderListener;

/**
 * @author xpenatan
 */
public class JoltLoader {

    /*[-JNI;-NATIVE]
        #include "JoltCustom.h"
    */

    public static void init(JParserLibraryLoaderListener listener) {
        JParserLibraryLoader.load("jolt", listener);
    }
}