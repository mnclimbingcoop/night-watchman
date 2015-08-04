package com.mnclimbingcoop.compresssion

import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

class StringCompressor {

    static String decompress(String input) {
        if (!input) { return input }
        try {
            ByteArrayInputStream fis = new ByteArrayInputStream(input.decodeBase64())
            GZIPInputStream gis = new GZIPInputStream(fis)
            ByteArrayOutputStream fos = new ByteArrayOutputStream()

            byte[] buffer = new byte[1024]
            int len
            while ((len = gis.read(buffer)) != -1) {
                fos.write(buffer, 0, len)
            }
            gis.close()
            return fos.toString()
        } catch (IOException e) {
            e.printStackTrace()
        }
    }

    static String compress(String input) {
        if (!input) { return input }
        try {
            ByteArrayInputStream fis = new ByteArrayInputStream(input.bytes)
            ByteArrayOutputStream fos = new ByteArrayOutputStream()
            GZIPOutputStream gzipOS = new GZIPOutputStream(fos)

            byte[] buffer = new byte[1024]
            int len
            while ((len=fis.read(buffer)) != -1) {
                gzipOS.write(buffer, 0, len)
            }
            gzipOS.close()
            fis.close()
            return fos.toByteArray().encodeBase64()

        } catch (IOException e) {
            e.printStackTrace()
        }
    }

}
