package a_evan.zhku.pnt_v2.database;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * 文件操作工具类
 */
public class FileAccessor {
    static Context c;
    public static final String TAG = FileAccessor.class.getName();
    public static final String APP_ROOT_STORE = "newsmedia";

    public static final String APP_DRAFT_PERSONAL_STORE = "personaldraft";
    public static final String APP_DRAFT_GROUP_STORE = "groupdraft";
    public static final String APP_DRAFT_DEPARTMENT_STORE = "departmentdraft";
    public static final String APP_DRAFT_PUBLIC_STORE = "publicdraft";

    public  FileAccessor(Context c)
    {
        this.c=c;
    }
    public  FileAccessor(){}
    /**
     * 创建或取得本地文件目录
     *
     * @param cx        Context
     * @param subFolder 文件目录名
     * @return 文件对象
     */
    public File getAppMediaStore(Context cx, String subFolder) {
        if (!checkAppFileStore(cx)) {
            return null;
        }

        if (!TextUtils.isEmpty(subFolder)) {
            return createSubFileStore(cx, subFolder);
        } else {
            return null;
        }
    }


    /**
     * 创建不带扩展名的随机文件名
     *
     * @param filetype 文件类型(txt, photo, vidoe, audio, rich)
     * @return 文件名
     */
    public static String createFileName(String filetype) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());

        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        return filetype + "_" + y + m + d + hour + minute + second;
    }



    /**
     * 拷贝文件
     *
     * @param filePath
     * @param destPath
     * @return true：成功 false:失败
     */
    public static boolean copyFile(String filePath, String destPath) {
        File originFile = new File(filePath);

        if (!originFile.exists()) {
            return false;
        }
        File destFile = new File(destPath);
        BufferedInputStream reader = null;
        BufferedOutputStream writer = null;
        try {
            if (!destFile.exists()) {
                destFile.createNewFile();
            }
            reader = new BufferedInputStream(new FileInputStream(originFile));
            writer = new BufferedOutputStream(new FileOutputStream(destFile));
            byte[] buffer = new byte[1024];
            int length;
            while ((length = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, length);
            }
        } catch (Exception exception) {
            return false;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
        }
        return true;
    }

    /**
     * 读取文件内容
     * @param path
     * @return 文件内容
     */
    public static String readContentByFile(String path) {
        BufferedReader reader = null;
        String line = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new FileReader(file));
                while ((line = reader.readLine()) != null) {
                    sb.append(line.trim());
                }
                return sb.toString().trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 通过文件全路径来获取文件名
     * @param pathName
     * @return 文件名
     */
    public static String getFileName(String pathName) {

        int start = pathName.lastIndexOf("/");
        if (start != -1) {
            return pathName.substring(start + 1, pathName.length());
        }
        return pathName;

    }

    /**
     * 获取外置存储卡的APP本地文件存储路径
     * @return PP本地文件存储路径
     */
    public static String getAppExternalStorePath(Context cx) {
        if (isExistExternalStore()) {
            String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + cx.getPackageName() + "/.newsedit";
            return storePath;
        }
        return null;
    }

    /**
     * 获取外置存储卡的路径
     *
     * @return 外置存储卡的路径
     */
    public static String getExternalStorePath() {
        if (isExistExternalStore()) {
            String storePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            return storePath;
        }
        return null;
    }

    /**
     * 是否有外存卡
     * @return true：有 false:没有
     */
    public static boolean isExistExternalStore() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 删除多个文件
     *
     * @param filePaths
     * @return true：成功 false:失败
     */
    public static void delFiles(ArrayList<String> filePaths) {
        for (String url : filePaths) {
            if (!TextUtils.isEmpty(url))
                delFile(url);
        }
    }

    /**
     * 删除一个文件
     *
     * @param filePath
     * @return true：成功 false:失败
     */
    public static boolean delFile(String filePath) {
        File file = new File(filePath);
        if (file == null || !file.exists()) {
            return true;
        }

        return file.delete();
    }

    /**
     * @param root
     * @param srcName
     * @param destName
     */
    public static void renameTo(String root, String srcName, String destName) {
        if (TextUtils.isEmpty(root) || TextUtils.isEmpty(srcName) || TextUtils.isEmpty(destName)) {
            return;
        }

        File srcFile = new File(root + srcName);
        File newPath = new File(root + destName);

        if (srcFile.exists()) {
            srcFile.renameTo(newPath);
        }
    }

    /**
     * 创建APP本地文件存储子目录
     *
     * @param cx Context
     * @return true：成功 false:失败
     */
    private File createSubFileStore(Context cx, String subFolder) {
        if (!checkAppFileStore(cx)) {
            return null;
        }

        File directory = new File(getAppExternalStorePath(cx),
                APP_ROOT_STORE + "/" + subFolder);
        if (!directory.exists() && !directory.mkdirs()) {
            return null;
        }
        return directory;
    }

    /**
     * 检查或创建APP本地文件存储目录
     *
     * @param cx Context
     * @return true：成功 false:失败
     */
    private boolean checkAppFileStore(Context cx) {
        if (!isExistExternalStore()) {
            return false;
        }
        File directory = new File(getAppExternalStorePath(cx),
                APP_ROOT_STORE);
        if (!directory.exists() && !directory.mkdirs()) {
            return false;
        }
        return true;
    }

    /**
     * 在SD卡上创建文件
     *
     * @throws IOException
     */
    public void createSDFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    /**
     * 写入内容到SD卡中的txt文本中 str为内容
     */
    public void writeSDFile(String str, String fileName) {
        try {
            FileWriter fw = new FileWriter(fileName);
            File f = new File(fileName);
            fw.write(str);
            FileOutputStream os = new FileOutputStream(f);
            DataOutputStream out = new DataOutputStream(os);
            out.writeShort(2);
            out.writeUTF("");
            System.out.println(out);
            fw.flush();
            fw.close();
            System.out.println(fw);
        } catch (Exception e) {
        }
    }

    /**
     * 读取SD卡中文本文件
     *
     * @param fileName
     * @return
     */
    public static String readSDFile(String fileName) {

        StringBuffer sb = new StringBuffer();
        File file = new File(fileName);
        try {
            FileInputStream fis = new FileInputStream(file);
            int c;
            while ((c = fis.read()) != -1) {
                sb.append((char) c);
            }
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    /*
     *
     * * 获取本地图片
     *
     * */

    public Bitmap pathToBitmap(String path) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
//获取资源图片
        return BitmapFactory.decodeFile(path, opt);
    }

    /**
     * 删除SD卡上的文件
     *
     * @param fileName
     */
    public boolean deleteSDFile(String fileName) {
        File file = new File(fileName);
        if (file == null || !file.exists() || file.isDirectory())
            return false;
        return file.delete();
    }

    /**
     * 将图片内容解析成字节数组
     *
     * @return byte[]
     * @throws Exception
     */
    public static byte[] readStream(String fllePath) throws Exception {
        InputStream inStream = new FileInputStream(new File(fllePath));
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;
    }
    /**
     * 将字节数组转换为ImageView可调用的Bitmap对象
     * @param bytes
     * @return Bitmap
     */
    public static Bitmap getPicFromBytes(byte[] bytes) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
                        opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }

    public Bitmap byteToBitmap (byte[] b)
    {
        if (b.length != 0) {

            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    public static byte[] File2byte(String filePath)
    {
        byte[] buffer = null;
        try
        {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return buffer;
    }

    public static File byte2file(byte[] filebyte) throws Exception
    {
        File file = File.createTempFile("yuan", "mp4", c.getCacheDir()); //生成临时文件
        OutputStream output = new FileOutputStream(file);

        BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);

        bufferedOutput.write(filebyte);
        output.close();
        bufferedOutput.close();
        file.deleteOnExit();
        return file;
    }
}