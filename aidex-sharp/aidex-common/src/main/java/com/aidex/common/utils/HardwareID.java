package com.aidex.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HardwareID {
    private static String a = null;

    public static void main(String[] args) {
//        System.out.println(HardwareID.getHardwareIDFromEthernetAddress()) ;//根据以太网地址生成HardwareID
//        System.out.println(HardwareID.getHardwareIDFromHostName()) ;//根据主机名生成HardwareID
//        System.out.println(HardwareID.getHardwareIDFromVolumeSerialNumber()) ;//根据卷序列号HardwareID


    }
    public HardwareID() {
    }

    public static String getHardwareIDFromHostName() {
        Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "start", "");

        try {
            UUID var0 = UUID.nameUUIDFromBytes(InetAddress.getLocalHost().getHostName().getBytes("UTF-8"));
            return "1" + var0.toString();
        } catch (UnknownHostException var1) {
            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG101", var1);
        } catch (UnsupportedEncodingException var2) {
            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG102", var2);
        } catch (Exception var3) {
            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG147", var3);
        }

        return null;
    }

    public static String getHardwareIDFromEthernetAddress() {
        Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "start", "");

        try {
            String var0 = null;
            if (System.getProperty("os.name").toLowerCase(Locale.US).startsWith("windows")) {
                NetworkInterface var1;
                if (!(var1 = NetworkInterface.getByInetAddress(InetAddress.getLocalHost())).isLoopback() && !var1.isVirtual() && !var1.isPointToPoint() && var1.getHardwareAddress() != null) {
                    var0 = Hex.encodeHexString((byte[])var1.getHardwareAddress());
                }

                if (var0 == null) {
                    var0 = a();
                }
            } else {
                var0 = a();
            }

            if (var0 != null && var0.length() > 11) {
                UUID var6 = UUID.nameUUIDFromBytes(var0.getBytes("UTF-8"));
                return "2" + var6.toString();
            }
        } catch (SocketException var2) {
            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG103", var2);
        } catch (UnknownHostException var3) {
            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG104", var3);
        } catch (UnsupportedEncodingException var4) {
            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG105", var4);
        } catch (Exception var5) {
            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG146", var5);
        }

        return null;
    }

    public static String getHardwareIDFromVolumeSerialNumber() {
        Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "start", "");
        if (System.getProperty("os.name").toLowerCase(Locale.US).startsWith("windows")) {
            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "start " + System.getProperty("os.name"), "");
            BufferedReader var0 = null;
            InputStreamReader var1 = null;
            boolean var10 = false;

            label215: {
                String var19;
                label216: {
                    try {
                        var10 = true;
                        Process var2 = Runtime.getRuntime().exec("cmd /C dir c:\\");
                        var1 = new InputStreamReader(var2.getInputStream(), "UTF-8");
                        (var0 = new BufferedReader(var1)).readLine();
                        if ((var19 = var0.readLine()) != null) {
                            if ((var19 = var19.substring(var19.length() - 9, var19.length())).length() > 1) {
                                try {
                                    UUID var20 = UUID.nameUUIDFromBytes(var19.getBytes("UTF-8"));
                                    var19 = "3" + var20.toString();
                                    var10 = false;
                                    break label216;
                                } catch (UnsupportedEncodingException var15) {
                                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG145" + System.getProperty("os.name"), "");
                                    var10 = false;
                                }
                            } else {
                                var10 = false;
                            }
                        } else {
                            var10 = false;
                        }
                    } catch (Exception var16) {
                        Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG106", var16);
                        var10 = false;
                        break label215;
                    } finally {
                        if (var10) {
                            try {
                                if (var0 != null) {
                                    var0.close();
                                }

                                if (var1 != null) {
                                    var1.close();
                                }
                            } catch (Exception var11) {
                                Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG107", var11);
                            }

                        }
                    }

                    try {
                        var0.close();
                        var1.close();
                    } catch (Exception var13) {
                        Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG107", var13);
                    }

                    return null;
                }

                try {
                    var0.close();
                    var1.close();
                } catch (Exception var12) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG107", var12);
                }

                return var19;
            }

            try {
                if (var0 != null) {
                    var0.close();
                }

                if (var1 != null) {
                    var1.close();
                }
            } catch (Exception var14) {
                Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG107", var14);
            }
        } else {
            if (System.getProperty("os.name").toLowerCase(Locale.US).startsWith("linux")) {
                Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "start " + System.getProperty("os.name"), "");
                String var18;
                if ((var18 = i()) == null) {
                    var18 = j();
                }

                if (var18 == null) {
                    var18 = k();
                }

                if (var18 == null) {
                    var18 = l();
                }

                if (var18 == null) {
                    var18 = m();
                }

                if (var18 == null) {
                    var18 = n();
                }

                if (var18 == null) {
                    var18 = o();
                }

                if (var18 == null) {
                    var18 = p();
                }

                return var18;
            }

            if (System.getProperty("os.name").toLowerCase(Locale.US).startsWith("mac")) {
                return q();
            }
        }

        return null;
    }

    public static boolean isVirtual() {
        Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "start", "");
        boolean var0 = false;
        if (System.getProperty("os.name").toLowerCase(Locale.US).startsWith("windows")) {
            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "start " + System.getProperty("os.name"), "");
            File var1 = null;
            InputStream var2 = null;
            FileOutputStream var3 = null;
            BufferedReader var4 = null;
            InputStreamReader var5 = null;

            try {
                var1 = File.createTempFile("tmp", ".exe", new File(System.getProperty("user.home")));
            } catch (IOException var26) {
                Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG114", var26);

                try {
                    var1 = File.createTempFile("tmp", ".exe");
                } catch (IOException var25) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG115", var25);
                    File var8 = new File(".");

                    try {
                        var1 = File.createTempFile("tmp", ".exe", var8.getCanonicalFile());
                    } catch (Exception var24) {
                        Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG116", var24);
                    }
                }
            }

            if (var1 != null) {
                var1.deleteOnExit();
            }

            InputStream var6 = null;
            boolean var19 = false;

            label366: {
                try {
                    var19 = true;
                    var2 = HardwareID.class.getResourceAsStream("d.bfi");
                    byte[] var7 = new byte[8192];
                    var3 = new FileOutputStream(var1);

                    int var30;
                    while((var30 = var2.read(var7)) != -1) {
                        var3.write(var7, 0, var30);
                    }

                    var3.flush();
                    var3.close();
                    var2.close();
                    var6 = Runtime.getRuntime().exec(new String[]{var1.getAbsolutePath()}).getInputStream();
                    var5 = new InputStreamReader(var6, "UTF-8");
                    var4 = new BufferedReader(var5);
                    String var31 = null;
                    boolean var9 = false;

                    String var29;
                    while((var29 = var4.readLine()) != null) {
                        if (!var9 && var29.startsWith("Product Id =")) {
                            var31 = var29;
                            var9 = true;
                        }
                    }

                    try {
                        Thread.sleep(50L);
                    } catch (InterruptedException var23) {
                        Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG117", var23);
                    }

                    var6.close();
                    if (var31 != null) {
                        if (var31.toLowerCase(Locale.US).indexOf("vbox") != -1) {
                            var0 = true;
                            a = "VirtualBox";
                            var19 = false;
                        } else if (var31.toLowerCase().indexOf("virtual hd") != -1) {
                            var0 = true;
                            a = "Hyper-V";
                            var19 = false;
                        } else if (var31.toLowerCase(Locale.US).indexOf("vmware") != -1) {
                            var0 = true;
                            a = "VMware";
                            var19 = false;
                        } else if (var31.toLowerCase(Locale.US).indexOf("qemu") != -1) {
                            var0 = true;
                            a = "QEMU";
                            var19 = false;
                        } else if (var31.toLowerCase(Locale.US).indexOf("xen") != -1) {
                            var0 = true;
                            a = "Xen";
                            var19 = false;
                        } else {
                            var19 = false;
                        }
                    } else {
                        var19 = false;
                    }
                    break label366;
                } catch (IOException var27) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG118", var27);
                    var19 = false;
                } finally {
                    if (var19) {
                        try {
                            if (var2 != null) {
                                var2.close();
                            }

                            if (var6 != null) {
                                var6.close();
                            }

                            if (var3 != null) {
                                var3.close();
                            }

                            if (var4 != null) {
                                var4.close();
                            }

                            if (var5 != null) {
                                var5.close();
                            }

                            if (var1 != null) {
                                var1.delete();
                            }
                        } catch (IOException var20) {
                            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG144", "");
                        }

                    }
                }

                try {
                    if (var2 != null) {
                        var2.close();
                    }

                    if (var6 != null) {
                        var6.close();
                    }

                    if (var3 != null) {
                        var3.close();
                    }

                    if (var4 != null) {
                        var4.close();
                    }

                    if (var5 != null) {
                        var5.close();
                    }

                    if (var1 != null) {
                        var1.delete();
                        return var0;
                    }
                } catch (IOException var21) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG144", "");
                }

                return var0;
            }

            try {
                if (var2 != null) {
                    var2.close();
                }

                if (var6 != null) {
                    var6.close();
                }

                var3.close();
                var4.close();
                var5.close();
                if (var1 != null) {
                    var1.delete();
                }
            } catch (IOException var22) {
                Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG144", "");
            }
        } else if (System.getProperty("os.name").toLowerCase(Locale.US).startsWith("linux")) {
            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "start " + System.getProperty("os.name"), "");
            if (!(var0 = r())) {
                var0 = s();
            }

            if (!var0) {
                var0 = t();
            }

            if (!var0) {
                var0 = u();
            }
        } else if (System.getProperty("os.name").toLowerCase(Locale.US).startsWith("mac")) {
            var0 = v();
        }

        return var0;
    }

    public static String getVirtualizationSoftwareName() {
        Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "start", "");
        if (a != null) {
            return a;
        } else {
            isVirtual();
            return null;
        }
    }

    private static String a() throws SocketException {
        ArrayList var0 = new ArrayList();
        Enumeration var1 = NetworkInterface.getNetworkInterfaces();

        while(var1.hasMoreElements()) {
            NetworkInterface var2;
            if (!(var2 = (NetworkInterface)var1.nextElement()).isLoopback() && !var2.isVirtual() && !var2.isPointToPoint() && var2.getHardwareAddress() != null) {
                var0.add(Hex.encodeHexString((byte[])var2.getHardwareAddress()));
            }
        }

        if (var0.size() > 0) {
            Collections.sort(var0);
        }

        String var5 = null;
        StringBuilder var4 = new StringBuilder();

        for(int var3 = 0; var3 < var0.size(); ++var3) {
            var4.append((String)var0.get(var3));
        }

        if (var4.length() > 0) {
            var5 = var4.toString();
        }

        return var5;
    }

    private static String b() {
        BufferedReader var0 = null;
        boolean var10 = false;

        String var17;
        label163: {
            label159: {
                try {
                    var10 = true;
                    Process var1 = Runtime.getRuntime().exec("hdparm -i /dev/sda");
                    var0 = new BufferedReader(new InputStreamReader(var1.getInputStream(), "UTF-8"));
                    String var2 = null;
                    String var3 = null;

                    while((var17 = var0.readLine()) != null) {
                        if (var17.indexOf("Model=") != -1) {
                            var2 = var17;
                        }

                        if (var17.indexOf("SerialNo=") != -1) {
                            var3 = var17;
                            break;
                        }
                    }

                    if (var2 != null) {
                        if (var3 != null) {
                            var2 = var2.substring(var2.indexOf("=") + 1, var2.indexOf(","));
                            var3 = var3.substring(var3.lastIndexOf("=") + 1, var3.length());
                            if (var2.length() > 1) {
                                if (var3.length() > 1) {
                                    UUID var18 = UUID.nameUUIDFromBytes((var2 + " " + var3).getBytes("UTF-8"));
                                    var17 = "4" + var18.toString();
                                    var10 = false;
                                    break label163;
                                }

                                var10 = false;
                            } else {
                                var10 = false;
                            }
                        } else {
                            var10 = false;
                        }
                    } else {
                        var10 = false;
                    }
                    break label159;
                } catch (IOException var15) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG119", var15);
                    var10 = false;
                } finally {
                    if (var10) {
                        try {
                            if (var0 != null) {
                                var0.close();
                            }
                        } catch (IOException var11) {
                            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG120", var11);
                        }

                    }
                }

                try {
                    if (var0 != null) {
                        var0.close();
                        return null;
                    }
                } catch (IOException var13) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG120", var13);
                }

                return null;
            }

            try {
                var0.close();
            } catch (IOException var14) {
                Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG120", var14);
            }

            return null;
        }

        try {
            var0.close();
        } catch (IOException var12) {
            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG120", var12);
        }

        return var17;
    }

    private static String c() {
        BufferedReader var0 = null;
        boolean var10 = false;

        String var17;
        label163: {
            label164: {
                try {
                    var10 = true;
                    Process var1 = Runtime.getRuntime().exec("hdparm -i /dev/hda");
                    var0 = new BufferedReader(new InputStreamReader(var1.getInputStream(), "UTF-8"));
                    String var2 = null;
                    String var3 = null;

                    while((var17 = var0.readLine()) != null) {
                        if (var17.indexOf("Model=") != -1) {
                            var2 = var17;
                        }

                        if (var17.indexOf("SerialNo=") != -1) {
                            var3 = var17;
                            break;
                        }
                    }

                    if (var2 != null) {
                        if (var3 != null) {
                            var2 = var2.substring(var2.indexOf("=") + 1, var2.indexOf(","));
                            var3 = var3.substring(var3.lastIndexOf("=") + 1, var3.length());
                            if (var2.length() > 1) {
                                if (var3.length() > 1) {
                                    UUID var18 = UUID.nameUUIDFromBytes((var2 + " " + var3).getBytes("UTF-8"));
                                    var17 = "4" + var18.toString();
                                    var10 = false;
                                    break label163;
                                }

                                var10 = false;
                            } else {
                                var10 = false;
                            }
                        } else {
                            var10 = false;
                        }
                    } else {
                        var10 = false;
                    }
                    break label164;
                } catch (Exception var15) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG121", var15);
                    var10 = false;
                } finally {
                    if (var10) {
                        try {
                            if (var0 != null) {
                                var0.close();
                            }
                        } catch (IOException var11) {
                            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG122", var11);
                        }

                    }
                }

                try {
                    if (var0 != null) {
                        var0.close();
                        return null;
                    }
                } catch (IOException var13) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG122", var13);
                }

                return null;
            }

            try {
                var0.close();
            } catch (IOException var14) {
                Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG122", var14);
            }

            return null;
        }

        try {
            var0.close();
        } catch (IOException var12) {
            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG122", var12);
        }

        return var17;
    }

    private static String d() {
        BufferedReader var0 = null;
        boolean var19 = false;

        String var29;
        label305: {
            label306: {
                label320: {
                    label321: {
                        label307: {
                            try {
                                label313: {
                                    var19 = true;
                                    Process var1 = Runtime.getRuntime().exec("ls -l /dev/disk/by-id/ | grep sda");
                                    var0 = new BufferedReader(new InputStreamReader(var1.getInputStream(), "UTF-8"));
                                    String var2 = null;
                                    String var3 = null;
                                    String var4 = null;
                                    String var5 = null;
                                    String var6 = null;
                                    String var7 = null;
                                    String var8 = null;
                                    String var9 = null;

                                    while((var29 = var0.readLine()) != null) {
                                        if (var29.indexOf("scsi-SATA") != -1 && var29.indexOf("part") == -1) {
                                            var2 = var29;
                                            var3 = var29;
                                            break;
                                        }

                                        if (var29.indexOf("scsi") != -1 && var29.indexOf("part") == -1) {
                                            var4 = var29;
                                            var5 = var29;
                                            break;
                                        }

                                        if (var29.indexOf("VBOX_HARDDISK") != -1 && var29.indexOf("part") == -1) {
                                            var6 = var29;
                                            var7 = var29;
                                            break;
                                        }

                                        if (var29.indexOf("ata-") != -1 && var29.indexOf("part") == -1) {
                                            var8 = var29;
                                            var9 = var29;
                                            break;
                                        }
                                    }

                                    UUID var30;
                                    if (var2 != null && var3 != null) {
                                        var2 = var2.substring(var2.indexOf("_") + 1, var2.lastIndexOf("_"));
                                        var3 = var3.substring(var3.lastIndexOf("_") + 1, var3.indexOf("->") - 1);
                                        if (var2.length() > 1 && var3.length() > 1) {
                                            var30 = UUID.nameUUIDFromBytes((var2 + " " + var3).getBytes("UTF-8"));
                                            var29 = "4" + var30.toString();
                                            var19 = false;
                                            break label305;
                                        }
                                    }

                                    if (var4 != null && var5 != null) {
                                        var5 = var4 = var4.substring(var4.indexOf("scsi-") + 5, var4.indexOf("->") - 1);
                                        if (var4.length() > 1 && var5.length() > 1) {
                                            var30 = UUID.nameUUIDFromBytes((var4 + " " + var5).getBytes("UTF-8"));
                                            var29 = "4" + var30.toString();
                                            var19 = false;
                                            break label313;
                                        }
                                    }

                                    if (var6 != null && var7 != null) {
                                        var7 = var6 = var6.substring(var6.indexOf("HARDDISK_") + 9, var6.indexOf("->") - 1);
                                        if (var6.length() > 1 && var7.length() > 1) {
                                            var30 = UUID.nameUUIDFromBytes((var6 + " " + var7).getBytes("UTF-8"));
                                            var29 = "4" + var30.toString();
                                            var19 = false;
                                            break label306;
                                        }
                                    }

                                    if (var8 != null) {
                                        if (var9 != null) {
                                            var9 = var8 = var8.substring(var8.indexOf("ata-") + 4, var8.indexOf("->") - 1);
                                            if (var8.length() > 1) {
                                                if (var9.length() > 1) {
                                                    var30 = UUID.nameUUIDFromBytes((var8 + " " + var9).getBytes("UTF-8"));
                                                    var29 = "4" + var30.toString();
                                                    var19 = false;
                                                    break label320;
                                                }

                                                var19 = false;
                                            } else {
                                                var19 = false;
                                            }
                                        } else {
                                            var19 = false;
                                        }
                                    } else {
                                        var19 = false;
                                    }
                                    break label321;
                                }
                            } catch (Exception var27) {
                                Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG123", var27);
                                var19 = false;
                                break label307;
                            } finally {
                                if (var19) {
                                    try {
                                        if (var0 != null) {
                                            var0.close();
                                        }
                                    } catch (IOException var20) {
                                        Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG124", var20);
                                    }

                                }
                            }

                            try {
                                var0.close();
                            } catch (IOException var22) {
                                Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG124", var22);
                            }

                            return var29;
                        }

                        try {
                            if (var0 != null) {
                                var0.close();
                                return null;
                            }
                        } catch (IOException var25) {
                            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG124", var25);
                        }

                        return null;
                    }

                    try {
                        var0.close();
                    } catch (IOException var26) {
                        Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG124", var26);
                    }

                    return null;
                }

                try {
                    var0.close();
                } catch (IOException var24) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG124", var24);
                }

                return var29;
            }

            try {
                var0.close();
            } catch (IOException var23) {
                Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG124", var23);
            }

            return var29;
        }

        try {
            var0.close();
        } catch (IOException var21) {
            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG124", var21);
        }

        return var29;
    }

    private static String e() {
        BufferedReader var0 = null;
        boolean var10 = false;

        String var17;
        label160: {
            label156: {
                try {
                    var10 = true;
                    Process var1 = Runtime.getRuntime().exec("ls -l /dev/disk/by-id/ | grep c0d0");
                    var0 = new BufferedReader(new InputStreamReader(var1.getInputStream(), "UTF-8"));
                    String var2 = null;
                    String var3 = null;

                    while((var17 = var0.readLine()) != null) {
                        if (var17.indexOf("cciss") != -1 && var17.indexOf("part") == -1) {
                            var2 = var17;
                            var3 = var17;
                            break;
                        }
                    }

                    var0.close();
                    if (var2 != null) {
                        if (var3 != null) {
                            var3 = var2 = var2.substring(var2.indexOf("cciss-") + 6, var2.indexOf("->") - 1);
                            if (var2.length() > 1) {
                                if (var3.length() > 1) {
                                    UUID var18 = UUID.nameUUIDFromBytes((var2 + " " + var3).getBytes("UTF-8"));
                                    var17 = "4" + var18.toString();
                                    var10 = false;
                                    break label160;
                                }

                                var10 = false;
                            } else {
                                var10 = false;
                            }
                        } else {
                            var10 = false;
                        }
                    } else {
                        var10 = false;
                    }
                    break label156;
                } catch (Exception var15) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG125", var15);
                    var10 = false;
                } finally {
                    if (var10) {
                        try {
                            if (var0 != null) {
                                var0.close();
                            }
                        } catch (IOException var11) {
                            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG126", var11);
                        }

                    }
                }

                try {
                    if (var0 != null) {
                        var0.close();
                        return null;
                    }
                } catch (IOException var13) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG126", var13);
                }

                return null;
            }

            try {
                var0.close();
            } catch (IOException var14) {
                Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG126", var14);
            }

            return null;
        }

        try {
            var0.close();
        } catch (IOException var12) {
            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG126", var12);
        }

        return var17;
    }

    private static String f() {
        BufferedReader var0 = null;
        boolean var10 = false;

        String var17;
        label160: {
            label161: {
                try {
                    var10 = true;
                    Process var1 = Runtime.getRuntime().exec("ls -l /dev/disk/by-id/ | grep mmc");
                    var0 = new BufferedReader(new InputStreamReader(var1.getInputStream(), "UTF-8"));
                    String var2 = null;
                    String var3 = null;

                    while((var17 = var0.readLine()) != null) {
                        if (var17.indexOf("mmc") != -1 && var17.indexOf("part") == -1) {
                            var2 = var17;
                            var3 = var17;
                            break;
                        }
                    }

                    if (var2 != null) {
                        if (var3 != null) {
                            var2 = var2.substring(var2.indexOf("-") + 1, var2.lastIndexOf("_"));
                            var3 = var3.substring(var3.lastIndexOf("_") + 1, var3.indexOf("->") - 1);
                            if (var2.length() > 1) {
                                if (var3.length() > 1) {
                                    UUID var18 = UUID.nameUUIDFromBytes((var2 + " " + var3).getBytes("UTF-8"));
                                    var17 = "4" + var18.toString();
                                    var10 = false;
                                    break label160;
                                }

                                var10 = false;
                            } else {
                                var10 = false;
                            }
                        } else {
                            var10 = false;
                        }
                    } else {
                        var10 = false;
                    }
                    break label161;
                } catch (Exception var15) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG123", var15);
                    var10 = false;
                } finally {
                    if (var10) {
                        try {
                            if (var0 != null) {
                                var0.close();
                            }
                        } catch (IOException var11) {
                            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG124", var11);
                        }

                    }
                }

                try {
                    if (var0 != null) {
                        var0.close();
                        return null;
                    }
                } catch (IOException var13) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG124", var13);
                }

                return null;
            }

            try {
                var0.close();
            } catch (IOException var14) {
                Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG124", var14);
            }

            return null;
        }

        try {
            var0.close();
        } catch (IOException var12) {
            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG124", var12);
        }

        return var17;
    }

    private static String g() {
        BufferedReader var0 = null;
        boolean var10 = false;

        String var17;
        label161: {
            label157: {
                try {
                    var10 = true;
                    Process var1 = Runtime.getRuntime().exec("ls -l /dev/disk/by-id/ | grep nvme0n1");
                    var0 = new BufferedReader(new InputStreamReader(var1.getInputStream(), "UTF-8"));
                    String var2 = null;
                    String var3 = null;

                    while((var17 = var0.readLine()) != null) {
                        if (!var17.startsWith("nvme-eui") && var17.indexOf("nvme0n1") != -1 && var17.startsWith("nvme")) {
                            var2 = var17;
                            var3 = var17;
                            break;
                        }
                    }

                    if (var2 != null) {
                        if (var3 != null) {
                            var2 = var2.substring(var2.indexOf("-") + 1, var2.lastIndexOf("_"));
                            var3 = var3.substring(var3.lastIndexOf("_") + 1, var3.indexOf("->") - 1);
                            if (var2.length() > 1) {
                                if (var3.length() > 1) {
                                    UUID var18 = UUID.nameUUIDFromBytes((var2 + " " + var3).getBytes("UTF-8"));
                                    var17 = "4" + var18.toString();
                                    var10 = false;
                                    break label161;
                                }

                                var10 = false;
                            } else {
                                var10 = false;
                            }
                        } else {
                            var10 = false;
                        }
                    } else {
                        var10 = false;
                    }
                    break label157;
                } catch (Exception var15) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG123", var15);
                    var10 = false;
                } finally {
                    if (var10) {
                        try {
                            if (var0 != null) {
                                var0.close();
                            }
                        } catch (IOException var11) {
                            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG124", var11);
                        }

                    }
                }

                try {
                    if (var0 != null) {
                        var0.close();
                        return null;
                    }
                } catch (IOException var13) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG124", var13);
                }

                return null;
            }

            try {
                var0.close();
            } catch (IOException var14) {
                Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG124", var14);
            }

            return null;
        }

        try {
            var0.close();
        } catch (IOException var12) {
            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG124", var12);
        }

        return var17;
    }

    private static String h() {
        BufferedReader var0 = null;
        boolean var10 = false;

        String var17;
        label163: {
            label164: {
                try {
                    var10 = true;
                    Process var1 = Runtime.getRuntime().exec("system_profiler SPSerialATADataType SPSASDataType SPParallelSCSIDataType");
                    var0 = new BufferedReader(new InputStreamReader(var1.getInputStream(), "UTF-8"));
                    String var2 = null;
                    String var3 = null;

                    while((var17 = var0.readLine()) != null) {
                        if (var17.indexOf("Model:") != -1) {
                            var2 = var17;
                        }

                        if (var17.indexOf("Serial Number:") != -1) {
                            var3 = var17;
                            break;
                        }
                    }

                    if (var2 != null) {
                        if (var3 != null) {
                            var2 = var2.substring(var2.indexOf(":") + 2, var2.length());
                            var3 = var3.substring(var2.indexOf(":") + 2, var3.length());
                            if (var2.length() > 1) {
                                if (var3.length() > 1) {
                                    UUID var18 = UUID.nameUUIDFromBytes((var2 + " " + var3).getBytes("UTF-8"));
                                    var17 = "4" + var18.toString();
                                    var10 = false;
                                    break label163;
                                }

                                var10 = false;
                            } else {
                                var10 = false;
                            }
                        } else {
                            var10 = false;
                        }
                    } else {
                        var10 = false;
                    }
                    break label164;
                } catch (IOException var15) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG127", var15);
                    var10 = false;
                } finally {
                    if (var10) {
                        try {
                            if (var0 != null) {
                                var0.close();
                            }
                        } catch (IOException var11) {
                            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG128", var11);
                        }

                    }
                }

                try {
                    if (var0 != null) {
                        var0.close();
                        return null;
                    }
                } catch (IOException var13) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG128", var13);
                }

                return null;
            }

            try {
                var0.close();
            } catch (IOException var14) {
                Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG128", var14);
            }

            return null;
        }

        try {
            var0.close();
        } catch (IOException var12) {
            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG128", var12);
        }

        return var17;
    }

    private static String i() {
        BufferedReader var0 = null;
        boolean var9 = false;

        label188: {
            String var16;
            label200: {
                try {
                    var9 = true;
                    Process var1 = Runtime.getRuntime().exec("ls -l /dev/disk/by-uuid/ | grep sda1;ls -l /dev/disk/by-uuid/ | grep hda1;ls -l /dev/disk/by-uuid/ | grep xvda1");
                    var0 = new BufferedReader(new InputStreamReader(var1.getInputStream(), "UTF-8"));
                    String var2 = null;

                    while(true) {
                        do {
                            if ((var16 = var0.readLine()) == null) {
                                if (var2 != null) {
                                    if (var2.length() <= 40) {
                                        var9 = false;
                                        break label188;
                                    }

                                    if ((var2 = var2.substring(var2.indexOf("->") - 37, var2.indexOf("->") - 1)).length() > 1) {
                                        if (var2.indexOf(":") != -1) {
                                            var2 = var2.substring(var2.length() - 16, var2.length());
                                        }

                                        if (var2.indexOf(":") != -1) {
                                            var2 = var2.substring(var2.length() - 9, var2.length());
                                        }

                                        UUID var17 = UUID.nameUUIDFromBytes(var2.getBytes("UTF-8"));
                                        var16 = "3" + var17.toString();
                                        var9 = false;
                                        break label200;
                                    }

                                    var9 = false;
                                    break label188;
                                }

                                var9 = false;
                                break label188;
                            }
                        } while(var16.indexOf("sda1") == -1 && var16.indexOf("hda1") == -1 && (var16.indexOf("xvda1") == -1 || var16.substring(var16.indexOf("->") - 37, var16.indexOf("->") - 1).length() <= 30));

                        var2 = var16;
                    }
                } catch (Exception var14) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG129", var14);
                    var9 = false;
                } finally {
                    if (var9) {
                        try {
                            if (var0 != null) {
                                var0.close();
                            }
                        } catch (IOException var10) {
                            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG130", var10);
                        }

                    }
                }

                try {
                    if (var0 != null) {
                        var0.close();
                        return null;
                    }
                } catch (IOException var12) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG130", var12);
                }

                return null;
            }

            try {
                var0.close();
            } catch (IOException var11) {
                Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG130", var11);
            }

            return var16;
        }

        try {
            var0.close();
        } catch (IOException var13) {
            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG130", var13);
        }

        return null;
    }

    private static String j() {
        BufferedReader var0 = null;
        boolean var9 = false;

        String var16;
        label171: {
            label167: {
                try {
                    var9 = true;
                    Process var1 = Runtime.getRuntime().exec("ls -l /dev/disk/by-uuid/ | grep c0d0p1;ls -l /dev/disk/by-uuid/ | grep vda1");
                    var0 = new BufferedReader(new InputStreamReader(var1.getInputStream(), "UTF-8"));
                    String var2 = null;

                    while(true) {
                        do {
                            if ((var16 = var0.readLine()) == null) {
                                if (var2 != null) {
                                    if (var2.length() > 40) {
                                        if ((var2 = var2.substring(var2.indexOf("->") - 37, var2.indexOf("->") - 1)).length() > 1) {
                                            UUID var17 = UUID.nameUUIDFromBytes(var2.getBytes("UTF-8"));
                                            var16 = "3" + var17.toString();
                                            var9 = false;
                                            break label171;
                                        }

                                        var9 = false;
                                    } else {
                                        var9 = false;
                                    }
                                } else {
                                    var9 = false;
                                }
                                break label167;
                            }
                        } while(var16.indexOf("c0d0p1") == -1 && (var16.indexOf("vda1") == -1 || var16.substring(var16.indexOf("->") - 37, var16.indexOf("->") - 1).length() <= 30));

                        var2 = var16;
                    }
                } catch (Exception var14) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG131", var14);
                    var9 = false;
                } finally {
                    if (var9) {
                        try {
                            if (var0 != null) {
                                var0.close();
                            }
                        } catch (IOException var10) {
                            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG132", var10);
                        }

                    }
                }

                try {
                    if (var0 != null) {
                        var0.close();
                        return null;
                    }
                } catch (IOException var12) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG132", var12);
                }

                return null;
            }

            try {
                var0.close();
            } catch (IOException var13) {
                Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG132", var13);
            }

            return null;
        }

        try {
            var0.close();
        } catch (IOException var11) {
            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG132", var11);
        }

        return var16;
    }

    private static String k() {
        BufferedReader var0 = null;
        boolean var9 = false;

        String var16;
        label180: {
            label176: {
                try {
                    var9 = true;
                    Process var1 = Runtime.getRuntime().exec("ls -l /dev/disk/by-uuid/ | grep mmcblk0p1;ls -l /dev/disk/by-uuid/ | grep xvda2");
                    var0 = new BufferedReader(new InputStreamReader(var1.getInputStream(), "UTF-8"));
                    String var2 = null;

                    while(true) {
                        do {
                            if ((var16 = var0.readLine()) == null) {
                                if (var2 != null) {
                                    if (var2.length() > 40) {
                                        if ((var2 = var2.substring(var2.indexOf("->") - 37, var2.indexOf("->") - 1)).length() > 1) {
                                            if (var2.indexOf(":") != -1) {
                                                var2 = var2.substring(var2.length() - 16, var2.length());
                                            }

                                            UUID var17 = UUID.nameUUIDFromBytes(var2.getBytes("UTF-8"));
                                            var16 = "3" + var17.toString();
                                            var9 = false;
                                            break label180;
                                        }

                                        var9 = false;
                                    } else {
                                        var9 = false;
                                    }
                                } else {
                                    var9 = false;
                                }
                                break label176;
                            }
                        } while(var16.indexOf("mmcblk0p1") == -1 && (var16.indexOf("xvda2") == -1 || var16.substring(var16.indexOf("->") - 37, var16.indexOf("->") - 1).length() <= 30));

                        var2 = var16;
                    }
                } catch (Exception var14) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG129", var14);
                    var9 = false;
                } finally {
                    if (var9) {
                        try {
                            if (var0 != null) {
                                var0.close();
                            }
                        } catch (IOException var10) {
                            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG130", var10);
                        }

                    }
                }

                try {
                    if (var0 != null) {
                        var0.close();
                        return null;
                    }
                } catch (IOException var12) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG130", var12);
                }

                return null;
            }

            try {
                var0.close();
            } catch (IOException var13) {
                Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG130", var13);
            }

            return null;
        }

        try {
            var0.close();
        } catch (IOException var11) {
            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG130", var11);
        }

        return var16;
    }

    private static String l() {
        BufferedReader var0 = null;
        boolean var9 = false;

        String var16;
        label162: {
            label158: {
                try {
                    var9 = true;
                    Process var1 = Runtime.getRuntime().exec("ls -l /dev/disk/by-uuid/ | grep vda2");
                    var0 = new BufferedReader(new InputStreamReader(var1.getInputStream(), "UTF-8"));
                    String var2 = null;

                    while((var16 = var0.readLine()) != null) {
                        if (var16.indexOf("vda2") != -1 && var16.substring(var16.indexOf("->") - 37, var16.indexOf("->") - 1).length() > 30) {
                            var2 = var16;
                        }
                    }

                    if (var2 != null) {
                        if (var2.length() > 40) {
                            if ((var2 = var2.substring(var2.indexOf("->") - 37, var2.indexOf("->") - 1)).length() > 1) {
                                if (var2.indexOf(":") != -1) {
                                    var2 = var2.substring(var2.length() - 16, var2.length());
                                }

                                UUID var17 = UUID.nameUUIDFromBytes(var2.getBytes("UTF-8"));
                                var16 = "3" + var17.toString();
                                var9 = false;
                                break label162;
                            }

                            var9 = false;
                        } else {
                            var9 = false;
                        }
                    } else {
                        var9 = false;
                    }
                    break label158;
                } catch (Exception var14) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG129", var14);
                    var9 = false;
                } finally {
                    if (var9) {
                        try {
                            if (var0 != null) {
                                var0.close();
                            }
                        } catch (IOException var10) {
                            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG130", var10);
                        }

                    }
                }

                try {
                    if (var0 != null) {
                        var0.close();
                        return null;
                    }
                } catch (IOException var12) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG130", var12);
                }

                return null;
            }

            try {
                var0.close();
            } catch (IOException var13) {
                Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG130", var13);
            }

            return null;
        }

        try {
            var0.close();
        } catch (IOException var11) {
            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG130", var11);
        }

        return var16;
    }

    private static String m() {
        BufferedReader var0 = null;
        boolean var9 = false;

        String var16;
        label162: {
            label158: {
                try {
                    var9 = true;
                    Process var1 = Runtime.getRuntime().exec("ls -l /dev/disk/by-uuid/ | grep sda2");
                    var0 = new BufferedReader(new InputStreamReader(var1.getInputStream(), "UTF-8"));
                    String var2 = null;

                    while((var16 = var0.readLine()) != null) {
                        if (var16.indexOf("sda2") != -1 && var16.substring(var16.indexOf("->") - 37, var16.indexOf("->") - 1).length() > 30) {
                            var2 = var16;
                        }
                    }

                    if (var2 != null) {
                        if (var2.length() > 40) {
                            if ((var2 = var2.substring(var2.indexOf("->") - 37, var2.indexOf("->") - 1)).length() > 1) {
                                if (var2.indexOf(":") != -1) {
                                    var2 = var2.substring(var2.length() - 16, var2.length());
                                }

                                UUID var17 = UUID.nameUUIDFromBytes(var2.getBytes("UTF-8"));
                                var16 = "3" + var17.toString();
                                var9 = false;
                                break label162;
                            }

                            var9 = false;
                        } else {
                            var9 = false;
                        }
                    } else {
                        var9 = false;
                    }
                    break label158;
                } catch (Exception var14) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG129", var14);
                    var9 = false;
                } finally {
                    if (var9) {
                        try {
                            if (var0 != null) {
                                var0.close();
                            }
                        } catch (IOException var10) {
                            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG130", var10);
                        }

                    }
                }

                try {
                    if (var0 != null) {
                        var0.close();
                        return null;
                    }
                } catch (IOException var12) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG130", var12);
                }

                return null;
            }

            try {
                var0.close();
            } catch (IOException var13) {
                Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG130", var13);
            }

            return null;
        }

        try {
            var0.close();
        } catch (IOException var11) {
            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG130", var11);
        }

        return var16;
    }

    private static String n() {
        BufferedReader var0 = null;
        boolean var9 = false;

        String var16;
        label162: {
            label163: {
                try {
                    var9 = true;
                    Process var1 = Runtime.getRuntime().exec("ls -l /dev/disk/by-uuid/ | grep dm-1");
                    var0 = new BufferedReader(new InputStreamReader(var1.getInputStream(), "UTF-8"));
                    String var2 = null;

                    while((var16 = var0.readLine()) != null) {
                        if (var16.indexOf("dm-1") != -1 && var16.substring(var16.indexOf("->") - 37, var16.indexOf("->") - 1).length() > 30) {
                            var2 = var16;
                        }
                    }

                    if (var2 != null) {
                        if (var2.length() > 40) {
                            if ((var2 = var2.substring(var2.indexOf("->") - 37, var2.indexOf("->") - 1)).length() > 1) {
                                if (var2.indexOf(":") != -1) {
                                    var2 = var2.substring(var2.length() - 16, var2.length());
                                }

                                UUID var17 = UUID.nameUUIDFromBytes(var2.getBytes("UTF-8"));
                                var16 = "3" + var17.toString();
                                var9 = false;
                                break label162;
                            }

                            var9 = false;
                        } else {
                            var9 = false;
                        }
                    } else {
                        var9 = false;
                    }
                    break label163;
                } catch (Exception var14) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG129", var14);
                    var9 = false;
                } finally {
                    if (var9) {
                        try {
                            if (var0 != null) {
                                var0.close();
                            }
                        } catch (IOException var10) {
                            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG130", var10);
                        }

                    }
                }

                try {
                    if (var0 != null) {
                        var0.close();
                        return null;
                    }
                } catch (IOException var12) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG130", var12);
                }

                return null;
            }

            try {
                var0.close();
            } catch (IOException var13) {
                Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG130", var13);
            }

            return null;
        }

        try {
            var0.close();
        } catch (IOException var11) {
            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG130", var11);
        }

        return var16;
    }

    private static String o() {
        BufferedReader var0 = null;
        boolean var9 = false;

        String var16;
        label162: {
            label158: {
                try {
                    var9 = true;
                    Process var1 = Runtime.getRuntime().exec("ls -l /dev/disk/by-uuid/ | grep dm-0");
                    var0 = new BufferedReader(new InputStreamReader(var1.getInputStream(), "UTF-8"));
                    String var2 = null;

                    while((var16 = var0.readLine()) != null) {
                        if (var16.indexOf("dm-0") != -1 && var16.substring(var16.indexOf("->") - 37, var16.indexOf("->") - 1).length() > 30) {
                            var2 = var16;
                        }
                    }

                    if (var2 != null) {
                        if (var2.length() > 40) {
                            if ((var2 = var2.substring(var2.indexOf("->") - 37, var2.indexOf("->") - 1)).length() > 1) {
                                if (var2.indexOf(":") != -1) {
                                    var2 = var2.substring(var2.length() - 16, var2.length());
                                }

                                UUID var17 = UUID.nameUUIDFromBytes(var2.getBytes("UTF-8"));
                                var16 = "3" + var17.toString();
                                var9 = false;
                                break label162;
                            }

                            var9 = false;
                        } else {
                            var9 = false;
                        }
                    } else {
                        var9 = false;
                    }
                    break label158;
                } catch (Exception var14) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG129", var14);
                    var9 = false;
                } finally {
                    if (var9) {
                        try {
                            if (var0 != null) {
                                var0.close();
                            }
                        } catch (IOException var10) {
                            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG130", var10);
                        }

                    }
                }

                try {
                    if (var0 != null) {
                        var0.close();
                        return null;
                    }
                } catch (IOException var12) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG130", var12);
                }

                return null;
            }

            try {
                var0.close();
            } catch (IOException var13) {
                Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG130", var13);
            }

            return null;
        }

        try {
            var0.close();
        } catch (IOException var11) {
            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG130", var11);
        }

        return var16;
    }

    private static String p() {
        BufferedReader var0 = null;
        boolean var9 = false;

        String var16;
        label162: {
            label158: {
                try {
                    var9 = true;
                    Process var1 = Runtime.getRuntime().exec("ls -l /dev/disk/by-uuid/ | grep nvme0n1p1;ls -l /dev/disk/by-uuid/ | grep nvme0n1p2");
                    var0 = new BufferedReader(new InputStreamReader(var1.getInputStream(), "UTF-8"));
                    String var2 = null;

                    while((var16 = var0.readLine()) != null) {
                        if (var16.indexOf("nvme0n1p") != -1 && var16.substring(var16.indexOf("->") - 37, var16.indexOf("->") - 1).length() > 30) {
                            var2 = var16;
                        }
                    }

                    if (var2 != null) {
                        if (var2.length() > 40) {
                            if ((var2 = var2.substring(var2.indexOf("->") - 37, var2.indexOf("->") - 1)).length() > 1) {
                                if (var2.indexOf(":") != -1) {
                                    var2 = var2.substring(var2.length() - 16, var2.length());
                                }

                                UUID var17 = UUID.nameUUIDFromBytes(var2.getBytes("UTF-8"));
                                var16 = "3" + var17.toString();
                                var9 = false;
                                break label162;
                            }

                            var9 = false;
                        } else {
                            var9 = false;
                        }
                    } else {
                        var9 = false;
                    }
                    break label158;
                } catch (Exception var14) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG129", var14);
                    var9 = false;
                } finally {
                    if (var9) {
                        try {
                            if (var0 != null) {
                                var0.close();
                            }
                        } catch (IOException var10) {
                            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG130", var10);
                        }

                    }
                }

                try {
                    if (var0 != null) {
                        var0.close();
                        return null;
                    }
                } catch (IOException var12) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG130", var12);
                }

                return null;
            }

            try {
                var0.close();
            } catch (IOException var13) {
                Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG130", var13);
            }

            return null;
        }

        try {
            var0.close();
        } catch (IOException var11) {
            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG130", var11);
        }

        return var16;
    }

    private static String q() {
        BufferedReader var0 = null;
        boolean var9 = false;

        String var16;
        label144: {
            label145: {
                try {
                    var9 = true;
                    Process var1 = Runtime.getRuntime().exec("diskutil info /");
                    var0 = new BufferedReader(new InputStreamReader(var1.getInputStream(), "UTF-8"));
                    String var2 = null;

                    while((var16 = var0.readLine()) != null) {
                        if (var16.indexOf("Volume UUID:") != -1) {
                            var2 = var16;
                            break;
                        }
                    }

                    if (var2 != null) {
                        if ((var2 = var2.substring(var2.length() - 36, var2.length())).length() > 1) {
                            UUID var17 = UUID.nameUUIDFromBytes(var2.getBytes("UTF-8"));
                            var16 = "3" + var17.toString();
                            var9 = false;
                            break label144;
                        }

                        var9 = false;
                    } else {
                        var9 = false;
                    }
                    break label145;
                } catch (IOException var14) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG133", var14);
                    var9 = false;
                } finally {
                    if (var9) {
                        try {
                            if (var0 != null) {
                                var0.close();
                            }
                        } catch (IOException var10) {
                            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG134", var10);
                        }

                    }
                }

                try {
                    if (var0 != null) {
                        var0.close();
                        return null;
                    }
                } catch (IOException var12) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG134", var12);
                }

                return null;
            }

            try {
                var0.close();
            } catch (IOException var13) {
                Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG134", var13);
            }

            return null;
        }

        try {
            var0.close();
        } catch (IOException var11) {
            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG134", var11);
        }

        return var16;
    }

    private static boolean r() {
        boolean var0 = false;
        String var1 = null;
        BufferedReader var2 = null;
        boolean var9 = false;

        label170: {
            label169: {
                try {
                    var9 = true;
                    Process var3 = Runtime.getRuntime().exec("dmesg |grep -i virtual");
                    var2 = new BufferedReader(new InputStreamReader(var3.getInputStream(), "UTF-8"));

                    String var15;
                    while((var15 = var2.readLine()) != null) {
                        if (var15.indexOf("Hyper-V") != -1) {
                            var0 = true;
                            var1 = "Hyper-V";
                        } else if (var15.indexOf("Detected virtualization 'microsoft'") != -1) {
                            var0 = true;
                            var1 = "Hyper-V";
                        } else if (var15.indexOf("Microsoft Vmbus") != -1) {
                            var0 = true;
                            var1 = "Hyper-V";
                        } else if (var15.indexOf("innotek GmbH VirtualBox") != -1) {
                            var0 = true;
                            var1 = "VirtualBox";
                        } else if (var15.indexOf("Detected virtualization 'oracle'") != -1) {
                            var0 = true;
                            var1 = "VirtualBox";
                        } else if (var15.indexOf("VirtualBox") != -1) {
                            var0 = true;
                            var1 = "VirtualBox";
                        } else if (var15.indexOf("VMware Virtual Platform") != -1) {
                            var0 = true;
                            var1 = "VMware";
                        } else if (var15.indexOf("VMware Virtual") != -1) {
                            var0 = true;
                            var1 = "VMware";
                        } else if (var15.indexOf("VMware") != -1) {
                            var0 = true;
                            var1 = "VMware";
                        } else if (var15.indexOf("QEMU") != -1) {
                            var0 = true;
                            var1 = "QEMU";
                        } else if (var15.indexOf("paravirtualized kernel on KVM") != -1) {
                            var0 = true;
                            var1 = "QEMU";
                        } else if (var15.indexOf("paravirtualized kernel on Xen") != -1) {
                            var0 = true;
                            var1 = "XEN";
                        }
                    }

                    var9 = false;
                    break label169;
                } catch (IOException var13) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG135", var13);
                    var9 = false;
                } finally {
                    if (var9) {
                        try {
                            if (var2 != null) {
                                var2.close();
                            }
                        } catch (IOException var10) {
                            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG136", var10);
                        }

                    }
                }

                try {
                    if (var2 != null) {
                        var2.close();
                    }
                } catch (IOException var11) {
                    Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG136", var11);
                }
                break label170;
            }

            try {
                var2.close();
            } catch (IOException var12) {
                Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG136", var12);
            }
        }

        if (var0) {
            a = var1;
        }

        return var0;
    }

    private static boolean s() {
        boolean var0 = false;
        BufferedReader var1 = null;
        boolean var8 = false;

        label123: {
            try {
                var8 = true;
                Process var2 = Runtime.getRuntime().exec("ls -l /dev/disk/by-id/");
                var1 = new BufferedReader(new InputStreamReader(var2.getInputStream(), "UTF-8"));

                String var14;
                while((var14 = var1.readLine()) != null) {
                    if (var14.indexOf("Virtual") != -1) {
                        var0 = true;
                        a = "Hyper-V";
                    } else if (var14.indexOf("VBOX") != -1) {
                        var0 = true;
                        a = "VirtualBox";
                    } else if (var14.indexOf("VMware") != -1) {
                        var0 = true;
                        a = "VMware";
                    } else if (var14.indexOf("QEMU") != -1) {
                        var0 = true;
                        a = "QEMU";
                    }
                }

                var8 = false;
                break label123;
            } catch (IOException var12) {
                Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG137", var12);
                var8 = false;
            } finally {
                if (var8) {
                    try {
                        if (var1 != null) {
                            var1.close();
                        }
                    } catch (IOException var9) {
                        Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG138", var9);
                    }

                }
            }

            try {
                if (var1 != null) {
                    var1.close();
                    return var0;
                }
            } catch (IOException var10) {
                Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG138", var10);
            }

            return var0;
        }

        try {
            var1.close();
        } catch (IOException var11) {
            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG138", var11);
        }

        return var0;
    }

    private static boolean t() {
        boolean var0 = false;
        BufferedReader var1 = null;
        boolean var8 = false;

        label113: {
            try {
                var8 = true;
                Process var2 = Runtime.getRuntime().exec("ls -l /dev/disk/by-uuid/ | grep xvda1;ls -l /dev/disk/by-uuid/ | grep vda1");
                var1 = new BufferedReader(new InputStreamReader(var2.getInputStream(), "UTF-8"));

                String var14;
                while((var14 = var1.readLine()) != null) {
                    if (var14.indexOf("xvda1") != -1 || var14.indexOf("vda1") != -1) {
                        var0 = true;
                        a = "Xen";
                    }
                }

                var8 = false;
                break label113;
            } catch (IOException var12) {
                Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG139", var12);
                var8 = false;
            } finally {
                if (var8) {
                    try {
                        if (var1 != null) {
                            var1.close();
                        }
                    } catch (IOException var9) {
                        Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG140", var9);
                    }

                }
            }

            try {
                if (var1 != null) {
                    var1.close();
                    return var0;
                }
            } catch (IOException var10) {
                Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG140", var10);
            }

            return var0;
        }

        try {
            var1.close();
        } catch (IOException var11) {
            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG140", var11);
        }

        return var0;
    }

    private static boolean u() {
        boolean var0 = false;
        BufferedReader var1 = null;
        boolean var8 = false;

        label105: {
            try {
                var8 = true;
                Process var2 = Runtime.getRuntime().exec("df | grep vzfs");
                var1 = new BufferedReader(new InputStreamReader(var2.getInputStream(), "UTF-8"));

                String var14;
                while((var14 = var1.readLine()) != null) {
                    if (var14.indexOf("vzfs") != -1) {
                        var0 = true;
                        a = "OpenVZ";
                    }
                }

                var8 = false;
                break label105;
            } catch (IOException var12) {
                Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG139", var12);
                var8 = false;
            } finally {
                if (var8) {
                    try {
                        if (var1 != null) {
                            var1.close();
                        }
                    } catch (IOException var9) {
                        Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG140", var9);
                    }

                }
            }

            try {
                if (var1 != null) {
                    var1.close();
                    return var0;
                }
            } catch (IOException var10) {
                Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG140", var10);
            }

            return var0;
        }

        try {
            var1.close();
        } catch (IOException var11) {
            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG140", var11);
        }

        return var0;
    }

    private static boolean v() {
        boolean var0 = false;
        BufferedReader var1 = null;
        boolean var9 = false;

        label138: {
            try {
                var9 = true;
                Process var2 = Runtime.getRuntime().exec("system_profiler SPSerialATADataType SPSASDataType SPParallelSCSIDataType");
                var1 = new BufferedReader(new InputStreamReader(var2.getInputStream(), "UTF-8"));
                String var3 = null;

                String var15;
                while((var15 = var1.readLine()) != null) {
                    if (var15.indexOf("Model:") != -1) {
                        var3 = var15;
                        break;
                    }
                }

                if (var3 != null) {
                    if (var15.indexOf("Virtual") != -1) {
                        var0 = true;
                        a = "Hyper-V";
                        var9 = false;
                    } else if (var15.indexOf("VBOX") != -1) {
                        var0 = true;
                        a = "VirtualBox";
                        var9 = false;
                    } else if (var15.indexOf("VMware") != -1) {
                        var0 = true;
                        a = "VMware";
                        var9 = false;
                    } else if (var15.indexOf("QEMU") != -1) {
                        var0 = true;
                        a = "QEMU";
                        var9 = false;
                    } else {
                        var9 = false;
                    }
                } else {
                    var9 = false;
                }
                break label138;
            } catch (IOException var13) {
                Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG141", var13);
                var9 = false;
            } finally {
                if (var9) {
                    try {
                        if (var1 != null) {
                            var1.close();
                        }
                    } catch (IOException var10) {
                        Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG142", var10);
                    }

                }
            }

            try {
                if (var1 != null) {
                    var1.close();
                    return var0;
                }
            } catch (IOException var11) {
                Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG142", var11);
            }

            return var0;
        }

        try {
            var1.close();
        } catch (IOException var12) {
            Logger.getLogger(HardwareID.class.getName()).log(Level.FINE, "MSG142", var12);
        }

        return var0;
    }
}
