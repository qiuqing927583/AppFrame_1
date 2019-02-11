package com.android.yawei.jhoa.utils;

import com.file.zip.ZipEntry;
import com.file.zip.ZipFile;
import com.file.zip.ZipOutputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

/**
 * 文件解压
 * Created by Yusz on 2018-3-2.
 */

public class UnzipFromAssets {


    /**
     * @param file      待解压文件
     * @param dir       解压后文件的存放目录,注意传路径时最后一定要加/
     * @throws IOException
     */
    public static void unzip(File file, String dir) throws IOException {
        if(!new File(dir).exists())
            new File(dir).mkdirs();
        ZipFile zipFile = new ZipFile(file, "GBK");//设置压缩文件的编码方式为GBK
        Enumeration<ZipEntry> entris = zipFile.getEntries();
        ZipEntry zipEntry = null;
        File tmpFile = null;
        BufferedOutputStream bos = null;
        InputStream is = null;
        byte[] buf = new byte[1024];
        int len = 0;
        while (entris.hasMoreElements()) {
            zipEntry = entris.nextElement();
            // 不进行文件夹的处理,些为特殊处理
            tmpFile = new File(dir + zipEntry.getName());
            if (zipEntry.isDirectory()) {//当前文件为目录
                if (!tmpFile.exists()) {
                    tmpFile.mkdirs();
                }
            } else {
                String filepathtemp = tmpFile.getPath().substring(0,tmpFile.getPath().lastIndexOf("/")+1);
                if(!new File(filepathtemp).exists()){
                    new File(filepathtemp).mkdirs();
                }
                if (!tmpFile.exists()) {
                    tmpFile.createNewFile();
                }
                is = zipFile.getInputStream(zipEntry);
                bos = new BufferedOutputStream(new FileOutputStream(tmpFile));
                while ((len = is.read(buf)) > 0) {
                    bos.write(buf, 0, len);
                }
                bos.flush();
                bos.close();
            }
        }
    }
    public static final String ENCODING_DEFAULT = "UTF-8";

    public static final int BUFFER_SIZE_DIFAULT = 1024;
    /**生成ZIP压缩包【建议异步执行】
     * @param srcFiles - 要压缩的文件数组
     * @param zipPath - 生成的Zip路径*/
    public static void makeZip(String[] srcFiles, String zipPath) throws Exception {
        makeZip(srcFiles, zipPath, ENCODING_DEFAULT);
    }

    /**生成ZIP压缩包【建议异步执行】
     * @param srcFiles - 要压缩的文件数组
     * @param zipPath - 生成的Zip路径
     * @param encoding - 编码格式*/
    public static void makeZip(String[] srcFiles, String zipPath, String encoding)
            throws Exception {
        ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipPath)));
        zipOut.setEncoding(encoding);
        for (int i = 0; i < srcFiles.length; i++) {
            File file = new File(srcFiles[i]);
            doZipFile(zipOut, file, file.getParent());
        }
        zipOut.flush();
        zipOut.close();
    }
    private static void doZipFile(ZipOutputStream zipOut, File file, String dirPath) throws FileNotFoundException, IOException {
        if (file.isFile()) {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            String zipName = file.getPath().substring(dirPath.length());
            while (zipName.charAt(0) == '\\' || zipName.charAt(0) == '/') {
                zipName = zipName.substring(1);
            }
            ZipEntry entry = new ZipEntry(zipName);
            zipOut.putNextEntry(entry);
            byte[] buff = new byte[BUFFER_SIZE_DIFAULT];
            int size;
            while ((size = bis.read(buff, 0, buff.length)) != -1) {
                zipOut.write(buff, 0, size);
            }
            zipOut.closeEntry();
            bis.close();
        } else {
            File[] subFiles = file.listFiles();
            for (File subFile : subFiles) {
                doZipFile(zipOut, subFile, dirPath);
            }
        }
    }

    //注释的原因解压中文乱码
//    /**
//     * 解压assets的zip压缩文件到指定目录
//     * @param context 上下文对象
//     * @param assetName 压缩文件名 xxx.zip
//     * @param outputDirectory 输出目录
//     * @param isReWrite 是否覆盖
//     * @throws Exception
//     */
//    public static void unZip(Context context, String assetName, String outputDirectory, boolean isReWrite) throws Exception {
//        // 创建解压目标目录
//        File file = new File(outputDirectory);
//        // 如果目标目录不存在，则创建
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        // 打开压缩文件
//        InputStream inputStream = context.getAssets().open(assetName);
//        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
//        // 读取一个进入点
//        ZipEntry zipEntry = zipInputStream.getNextEntry();
//        // 使用1Mbuffer
//        byte[] buffer = new byte[1024 * 1024];
//        // 解压时字节计数
//        int count = 0;
//        // 如果进入点为空说明已经遍历完所有压缩包中文件和目录
//        while (zipEntry != null) {
//            // 如果是一个目录
//            if (zipEntry.isDirectory()) {
//                file = new File(outputDirectory + File.separator + zipEntry.getName());
//                // 文件需要覆盖或者是文件不存在
//                if (isReWrite || !file.exists()) {
//                    file.mkdir();
//                }
//            } else {
//                // 如果是文件
//                file = new File(outputDirectory + File.separator + zipEntry.getName());
//                // 文件需要覆盖或者文件不存在，则解压文件
//                if (isReWrite || !file.exists()) {
//                    file.createNewFile();
//                    FileOutputStream fileOutputStream = new FileOutputStream(file);
//                    while ((count = zipInputStream.read(buffer)) > 0) {
//                        fileOutputStream.write(buffer, 0, count);
//                    }
//                    fileOutputStream.close();
//                }
//            }
//            // 定位到下一个文件入口
//            zipEntry = zipInputStream.getNextEntry();
//        }
//        zipInputStream.close();
//    }
//    /**
//     * 解压缩指定目录下
//     * 将zipFile文件解压到folderPath目录下.
//     * @param zipPath zip文件
//     * @param folderPath 解压到的地址
//     * @throws IOException
//     */
//    public static void upZipFile(String zipPath, String folderPath) throws IOException {
//        File zipFile = new File(zipPath);
//        if(!new File(folderPath).exists()){
//            new File(folderPath).mkdirs();
//        }
//        ZipFile zfile = new ZipFile(zipFile);
//        Enumeration zList = zfile.entries();
//        ZipEntry ze = null;
//        byte[] buf = new byte[1024];
//        while (zList.hasMoreElements()) {
//            ze = (ZipEntry) zList.nextElement();
//            if (ze.isDirectory()) {
//                String dirstr = folderPath + ze.getName();
//                dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
//                File f = new File(dirstr);
//                f.mkdir();
//                continue;
//            }
//            OutputStream os = new BufferedOutputStream(new FileOutputStream(getRealFileName(folderPath, ze.getName())));
//            InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
//            int readLen = 0;
//            while ((readLen = is.read(buf, 0, 1024)) != -1) {
//                os.write(buf, 0, readLen);
//            }
//            is.close();
//            os.close();
//        }
//        zfile.close();
//    }
//
//    /**
//     * 给定根目录，返回一个相对路径所对应的实际文件名.
//     * @param baseDir     指定根目录
//     * @param absFileName 相对路径名，来自于ZipEntry中的name
//     * @return java.io.File 实际的文件
//     */
//    public static File getRealFileName(String baseDir, String absFileName) {
//        String[] dirs = absFileName.split("/");
//        File ret = new File(baseDir);
//        String substr = null;
//        if (dirs.length > 1) {
//            for (int i = 0; i < dirs.length - 1; i++) {
//                substr = dirs[i];
//                try {
//                    substr = new String(substr.getBytes("8859_1"), "GB2312");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                ret = new File(ret, substr);
//
//            }
//            if (!ret.exists())
//                ret.mkdirs();
//            substr = dirs[dirs.length - 1];
//            try {
//                substr = new String(substr.getBytes("8859_1"), "GB2312");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            ret = new File(ret, substr);
//            return ret;
//        }
//        return ret;
//    }
}
