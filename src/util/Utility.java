package util;

import java.io.*;
import java.lang.ref.Reference;
import java.lang.reflect.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipFile;
/**
 * 常用方法集合
 * Created by qumiao on 14-1-15.
 */
public class Utility {
    public static final int APP_ITEM_MAX_COUNT = 100;
    /**
     * File buffer stream size.
     */
    public static final int FILE_STREAM_BUFFER_SIZE = 16 * 1024;
    public static final int SIZE_OF_INT = 4;
    public static final int SIZE_OF_LONG = 8;
    public static final String UTF8 = "UTF-8";
    // 用于格式化日期,作为日志文件名的一部分
    public static SimpleDateFormat TIME_LOG_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US);
    private Utility() {
    }
    /**
     * 比较两个对象是否相等（通过equals），并且避免NullPointerException
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean equalsSafely(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }
    /**
     * String to OutputStream
     *
     * @param str    String
     * @param stream OutputStream
     * @return success or not
     */
    public static boolean stringToStream(String str, OutputStream stream) {
        if (str == null) {
            return false;
        }
        byte[] data;
        try {
            data = str.getBytes("UTF-8");
            stream.write(data);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(stream);
        }
        return false;
    }
    /**
     * String to file
     *
     * @param str  String
     * @param file File
     * @return success or not
     */
    public static boolean stringToFile(String str, File file) {
        return stringToFile(str, file, false);
    }
    /**
     * String to file
     *
     * @param str    String
     * @param file   File
     * @param append is append mode or not
     * @return success or not
     */
    public static boolean stringToFile(String str, File file, boolean append) {
        OutputStream stream = null;
        try {
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            stream = new FileOutputStream(file, append);
            return stringToStream(str, stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(stream);
        }
    }
    /**
     * bytes to file
     *
     * @param data   bytes
     * @param file File
     * @return success or not
     */
    public static boolean bytesToFile(byte[] data, File file) {
        return bytesToFile(data, 0, data.length, file);
    }
    /**
     * bytes to file
     *
     * @param data   bytes
     * @param file File
     * @return success or not
     */
    public static boolean bytesToFile(byte[] data, int off, int len, File file) {
        return streamToFile(new ByteArrayInputStream(data, off, len), file, false);
    }
    public static byte[] fileToBytes(File file) {
        try {
            return streamToBytes(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * stream to file
     *
     * @param in   Inputstream
     * @param file File
     * @return success or not
     */
    public static boolean streamToFile(InputStream in, File file) {
        return streamToFile(in, file, false);
    }
    /**
     * stream to file
     *
     * @param in     Inputstream
     * @param file   File
     * @param append is append mode or not
     * @return success or not
     */
    public static boolean streamToFile(InputStream in, File file, boolean append) {
        OutputStream output = null;
        try {
            output = new BufferedOutputStream(new FileOutputStream(file, append));
            final byte[] buffer = new byte[FILE_STREAM_BUFFER_SIZE];
            int n;
            while (-1 != (n = in.read(buffer))) {
                output.write(buffer, 0, n);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Utility.close(output);
            Utility.close(in);
        }
        return false;
    }
    /**
     * stream to bytes
     *
     * @param is inputstream
     * @return bytes
     */
    public static byte[] streamToBytes(InputStream is) {
        try {
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            final byte[] buffer = new byte[FILE_STREAM_BUFFER_SIZE];
            int n;
            while (-1 != (n = is.read(buffer))) {
                output.write(buffer, 0, n);
            }
            return output.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(is);
        }
        return null;
    }

    public static int hashCodeSafely(Object obj) {
        return obj == null ? 0 : obj.hashCode();
    }
    /**
     * 获取不为空的字串
     *
     * @param sequence
     * @return
     */
    public static String getNotNullString(CharSequence sequence) {
        return sequence != null ? sequence.toString() : "";
    }

    /**
     * 关闭，并捕获IOException
     *
     * @param closeable Closeable
     */
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 关闭ZipFile。之所以提供这个方法是因为在某些操蛋的手机上（例如小米，预祝该公司早日倒闭），ZipFile居然木有实现Closeable
     *
     * @param zipFile ZipFile
     */
    public static void close(ZipFile zipFile) {
        if (zipFile != null) {
            try {
                zipFile.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }



    public static void close(RandomAccessFile file) {
        if (file != null) {
            try {
                file.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
    public static void close(ServerSocket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
    public static void close(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 通过反射修改（包括非public的）属性。在寻找该属性时，会一直递归到（但不包括）Object class。
     *
     * @param obj       调用该对象所在类的属性
     * @param fieldName 属性名
     * @param newValue  修改成该值
     * @return 如果成功找到并修改该属性，返回true，否则返回false
     */
    public static boolean modifyField(Object obj, String fieldName, Object newValue) {
        if (obj == null) {
            return false;
        }
        boolean retval = false;
        Field field = getField(obj, fieldName);
        if (field != null) {
            field.setAccessible(true);
            try {
                field.set(obj, newValue);
                retval = true;
            } catch (IllegalAccessException iae) {
                throw new IllegalArgumentException(fieldName, iae);
            } catch (ExceptionInInitializerError eiie) {
                throw new IllegalArgumentException(fieldName, eiie);
            }
        }
        return retval;
    }
    /**
     * 递归查找声明的属性，一直递归到（但不包括）Object class。
     *
     * @param object    指定对象
     * @param fieldName 属性名
     * @return 属性
     */
    public static Field getField(Object object, String fieldName) {
        Field field;
        Class<?> theClass = object.getClass();
        for (; theClass != Object.class; theClass = theClass.getSuperclass()) {
            try {
                field = theClass.getDeclaredField(fieldName);
                return field;
            } catch (NoSuchFieldException e) {  //SUPPRESS CHECKSTYLE
            } catch (SecurityException e) {
                throw new IllegalArgumentException(theClass.getName() + "." + theClass, e);
            }
        }
        return null;
    }
    /**
     * 调用一个对象的隐藏方法。
     *
     * @param obj        调用方法的对象.
     * @param methodName 方法名。
     * @param types      方法的参数类型。
     * @param args       方法的参数。
     * @return 如果调用成功，则返回true。
     */
    public static boolean invokeHideMethod(Object obj, String methodName, Class<?>[] types, Object[] args) {
        boolean hasInvoked = false;
        try {
            Method method = obj.getClass().getMethod(methodName, types);
            method.invoke(obj, args);
            hasInvoked = true;
        } catch (Exception ignore) {    // SUPPRESS CHECKSTYLE
        }
        return hasInvoked;
    }
    /**
     * 调用一个对象的隐藏方法。
     *
     * @param obj        调用方法的对象.
     * @param methodName 方法名。
     * @param types      方法的参数类型。
     * @param args       方法的参数。
     * @param result     如果该方法有返回值，则将返回值放入result[0]中；否则将result[0]置为null
     * @return 隐藏方法调用的返回值。
     */
    public static boolean invokeHideMethod(Object obj,
                                           String methodName, Class<?>[] types, Object[] args, Object[] result) {
        boolean retval = false;
        try {
            Method method = obj.getClass().getMethod(methodName, types);
            final Object invocationResult = method.invoke(obj, args);
            if (result != null && result.length > 0) {
                result[0] = invocationResult;
            }
            retval = true;
        } catch (Exception ignore) {    // SUPPRESS CHECKSTYLE
        }
        return retval;
    }
    /**
     * 反射调用（包括非public的）方法。在寻找该方法时，会一直递归到（但不包括）Object class。
     *
     * @param obj        调用该对象所在类的非public方法
     * @param methodName 方法名
     * @param result     如果该方法有返回值，则将返回值放入result[0]中；否则将result[0]置为null
     * @return 如果成功找到并调用该方法，返回true，否则返回false
     */
    public static boolean invokeMethod(Object obj, String methodName, Object[] result) {
        return invokeMethod(obj, methodName, null, result);
    }
    /**
     * 反射调用（包括非public的）方法。在寻找该方法时，会一直递归到（但不包括）Object class。
     *
     * @param obj        调用该对象所在类的非public方法
     * @param methodName 方法名
     * @param params     调用该方法需要的参数
     * @param result     如果该方法有返回值，则将返回值放入result[0]中；否则将result[0]置为null
     * @return 如果成功找到并调用该方法，返回true，否则返回false
     */
    public static boolean invokeMethod(Object obj, String methodName, Object[] params, Object[] result) { // SUPPRESS CHECKSTYLE
        Class<?>[] paramTypes = params == null ? null : new Class<?>[params.length];
        if (params != null) {
            for (int i = 0; i < params.length; ++i) {
                paramTypes[i] = params[i] == null ? null : params[i].getClass();
            }
        }
        return invokeMethod(obj, methodName, paramTypes, params, result);
    }
    /**
     * 反射调用（包括非public的）方法。在寻找该方法时，会一直递归到（但不包括）Object class。
     *
     * @param obj        调用该对象所在类的非public方法
     * @param methodName 方法名
     * @param paramTypes 该方法所有参数的类型
     * @param params     调用该方法需要的参数
     * @param result     如果该方法有返回值，则将返回值放入result[0]中；否则将result[0]置为null
     * @return 如果成功找到并调用该方法，返回true，否则返回false
     */
    public static boolean invokeMethod(Object obj, String methodName, Class<?>[] paramTypes, Object[] params, // SUPPRESS CHECKSTYLE
                                       Object[] result) { // SUPPRESS CHECKSTYLE
        if (obj == null) {
            return false;
        }
        boolean retval = false;
        Method method = getMethod(obj, methodName, paramTypes);
        // invoke
        if (method != null) {
            method.setAccessible(true);
            try {
                final Object invocationResult = method.invoke(obj, params);
                if (result != null && result.length > 0) {
                    result[0] = invocationResult;
                }
                retval = true;
            } catch (IllegalAccessException iae) {
                throw new IllegalArgumentException(methodName, iae);
            } catch (InvocationTargetException ite) {
                throw new IllegalArgumentException(methodName, ite);
            } catch (ExceptionInInitializerError eiie) {
                throw new IllegalArgumentException(methodName, eiie);
            }
        }
        return retval;
    }
    /**
     * 递归查找声明的方法，一直递归到（但不包括）Object class。
     *
     * @param object     指定对象
     * @param methodName 方法名
     * @param paramTypes 参数类型
     * @return 指定对象的方法
     */
    public static Method getMethod(Object object, String methodName, Class<?>[] paramTypes) {
        Method method;
        Class<?> theClass = object.getClass();
        for (; theClass != Object.class; theClass = theClass.getSuperclass()) {
            try {
                method = theClass.getDeclaredMethod(methodName, paramTypes);
                return method;
            } catch (NoSuchMethodException e) {  //SUPPRESS CHECKSTYLE
            } catch (SecurityException e) {
                throw new IllegalArgumentException(theClass.getName() + "." + methodName, e);
            }
        }
        return null;
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean 成功清除目录及子文件返回true；
     * 若途中删除某一文件或清除目录失败，则终止清除工作并返回false.
     */
    public static boolean deleteDir(File dir) {
        if (dir == null) {
            return false;
        }
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
    /**
     * 递归清空目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要清空的文件目录
     * @return boolean 成功清除目录及子文件返回true；
     * 若途中清空某一文件或清除目录失败，则终止清除工作并返回false.
     */
    public static boolean clearDir(File dir) {
        if (dir == null) {
            return false;
        }
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }
        return true;
    }
    /**
     * 获取设备上某个volume对应的存储路径
     *
     * @param volume 存储介质
     * @return 存储路径
     */
    public static String getVolumePath(Object volume) {
        String[] result = new String[1];
        if (Utility.invokeHideMethod(volume, "getPath", null, null, result)) {
            return result[0];
        }
        return "";
    }



    /**
     * 加上第一个参数
     *
     * @param url   在url附加参数
     * @param key   参数key（带"="）
     * @param value 参数value
     * @return 加完参数后的url.
     */
    private static String addFirstParam(String url, String key, String value) {
        return url + '?' + key + value;
    }
    /**
     * 追加参数
     *
     * @param url   在url附加参数
     * @param key   参数key（带"="）
     * @param value 参数value
     * @return 加完参数后的url.
     */
    private static String addFollowedParam(String url, String key, String value) {
        StringBuilder sb = new StringBuilder(url);
        if (!url.endsWith("&") && !url.endsWith("?")) {    //SUPPRESS CHECKSTYLE
            sb.append('&');
        }
        sb.append(key).append(value);
        return sb.toString();
    }
    /**
     * 替换参数值
     *
     * @param url        即将替换参数值的url
     * @param value      参数的新值
     * @param valueStart 原参数值在url中的位置
     * @return 替换完参数值的url
     */
    private static String replaceParam(String url, String value, int valueStart) {
        int valueEnd = url.indexOf('&', valueStart);
        if (valueEnd == -1) {
            valueEnd = url.length();
        }
        StringBuilder sb = new StringBuilder(url);
        sb.replace(valueStart, valueEnd, value);
        return sb.toString();
    }
    /**
     * 删除参数
     *
     * @param url       原url
     * @param paramName 参数名
     * @return 删除完参数后的url
     */
    public static String removeParam(String url, String paramName) {
        String value;
        String[] subUrlHolder = new String[1];
        String ret = removeSubUrl(url, subUrlHolder);
        if ((value = getQueryValue(new StringBuilder(ret), '?' + paramName + '=')) // SUPPRESS CHECKSTYLE
                != null) {
            ret = ret.replace(paramName + '=' + value, "");
            if (ret.endsWith("?")) {
                ret = ret.substring(0, ret.length() - 1);
            }
        } else if ((value = getQueryValue(new StringBuilder(ret), '&' + paramName + '='))    // SUPPRESS CHECKSTYLE
                != null) {
            ret = ret.replace('&' + paramName + '=' + value, "");
        }
        return ret + subUrlHolder[0];
    }
    /**
     * 获取url中指定参数的值
     *
     * @param url       Url
     * @param paramName 参数名
     * @return 参数值
     */
    public static String getParamValue(String url, String paramName) {
        String value;
        StringBuilder sb = new StringBuilder(removeSubUrl(url, null));
        if ((value = getQueryValue(sb, '?' + paramName + '=')) == null) {    // SUPPRESS CHECKSTYLE
            value = getQueryValue(sb, '&' + paramName + '=');
        }
        return value;
    }
    private static String removeSubUrl(String url, String[] subUrlHolder) {
        int indexHash = url.indexOf('#');
        if (indexHash < 0) { //没有#
            if (subUrlHolder != null) {
                subUrlHolder[0] = "";
            }
            return url;
        }
        if (subUrlHolder != null) {
            subUrlHolder[0] = url.substring(indexHash);
        }
        return url.substring(0, indexHash);
    }
    private static String getQueryValue(StringBuilder sb, String query) {
        int index = sb.indexOf(query);
        if (index != -1) {
            int startIndex = index + query.length();
            int endIndex = sb.indexOf("&", startIndex);
            if (endIndex == -1) {
                endIndex = sb.length();
            }
            return sb.substring(startIndex, endIndex);
        }
        return null;
    }

    /**
     * 将异常的StackTrace输出到字串中
     *
     * @param e 异常
     * @return StackTrace字串
     */
    public static String getPrintStackTrace(Throwable e) {
        if (e == null) {
            return "";
        }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        Throwable cause = e.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        return writer.toString();
    }
    /**
     * 获取IP信息。如果没有，默认返回127.0.0.1
     *
     * @return info
     */
    public static String getIpInfo() {
        return getIpInfo("127.0.0.1");
    }
    /**
     * 获取IP信息
     *
     * @return info
     */
    public static String getIpInfo(String defaultIp) {
        String ipInfo = null;
        System.setProperty("java.net.preferIPv6Addresses", "false");
        try {
            Enumeration<NetworkInterface> faces = NetworkInterface.getNetworkInterfaces();
            while (faces.hasMoreElements()) {
                Enumeration<InetAddress> addresses = faces.nextElement().getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        ipInfo = inetAddress.getHostAddress();
                        if (!ipInfo.contains("::")) {    //滤过ipv6形式
                            return ipInfo;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ipInfo != null ? ipInfo : defaultIp;
    }
    /**
     * 返回type类型代表的原始Class类型。
     *
     * @param type
     * @return
     */
    public static Class<?> getRawType(Type type) {
        if (type instanceof Class<?>) {
            // type is a normal class.
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            // I'm not exactly sure why getRawType() returns Type instead of Class.
            // Neal isn't either but suspects some pathological case related
            // to nested classes exists.
            Type rawType = parameterizedType.getRawType();
            //noinspection ConstantConditions
            return (Class<?>) rawType;
        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return Array.newInstance(getRawType(componentType), 0).getClass();
        } else if (type instanceof TypeVariable) {
            Type[] upperBoundTypes = ((TypeVariable) type).getBounds();
            if (upperBoundTypes.length == 1) {
                return getRawType(upperBoundTypes[0]);
            }
            // we could use the variable's bounds, but that won't work if there are multiple.
            // having a raw type that's more general than necessary is okay
            return Object.class;
        } else if (type instanceof WildcardType) {
            return getRawType(((WildcardType) type).getUpperBounds()[0]);
        } else {
            String className = type == null ? "null" : type.getClass().getName();
            throw new IllegalArgumentException("Expected a Class, ParameterizedType, or "
                    + "GenericArrayType, but <" + type + "> is of type " + className);
        }
    }

    /**
     * 从引用中获取具体值。若引用为空，则返回空
     *
     * @param reference 引用
     * @param <T>
     * @return
     */
    public static <T> T getValueFromReference(Reference<T> reference) {
        return reference == null ? null : reference.get();
    }
}