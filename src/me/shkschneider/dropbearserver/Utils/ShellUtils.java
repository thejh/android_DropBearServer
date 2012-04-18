/*
 * Muzikant <http://muzikant-android.blogspot.fr/2011/02/how-to-get-root-access-and-execute.html>
 */
package me.shkschneider.dropbearserver.Utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.util.Log;
import com.stericson.RootTools.RootTools;

public abstract class ShellUtils
{
	public static String TAG = "ShellUtils";

	public static String mBusyboxPath = null;
	public static ArrayList<String> commands = new ArrayList<String>();
	
	public static final Boolean mkdir(String path) {
		commands.add("mkdir " + path);
		return execute();
	}
	
	public static final Boolean chown(String path, String owner) {
		commands.add("chown " + owner + " " + path);
		return execute();
	}
	
	public static final Boolean chmod(String path, String chmod) {
		commands.add("chmod " + chmod + " " + path);
		return execute();
	}
	
	public static final Boolean touch(String path) {
		commands.add("echo -n '' > " + path);
		return execute();
	}
	
	public static final Boolean rm(String path) {
		commands.add("rm -r " + path);
		return execute();
	}
	
	public static final Boolean mv(String srcPath, String destPath) {
		commands.add("cat " + srcPath + " > " + destPath);
		commands.add("rm " + srcPath);
		return execute();
	}
	
	public static final Boolean cp(String srcPath, String destPath) {
		commands.add("cat " + srcPath + " > " + destPath);
		return execute();
	}
	
	public static final Boolean echoToFile(String text, String path) {
		commands.add("echo " + text + " > " + path);
		return execute();
	}
	
	public static final Boolean echoAppendToFile(String text, String path) {
		commands.add("echo " + text + " >> " + path);
		return execute();
	}
	
	public static final Boolean kill(int signal, int pid) {
		if (signal > 0 && pid > 0) {
			commands.add("kill -" + signal + " " + pid);
			return execute();
		}
		return false;
	}

	public static final boolean execute()
	{
		boolean retval = false;

		try {
			if (null != commands && commands.size() > 0) {
				Process suProcess = Runtime.getRuntime().exec("su");
				DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());
				for (String currCommand : commands) {
					Log.d(TAG, "# " + currCommand);
					os.writeBytes(currCommand + "\n");
					os.flush();
				}
				os.writeBytes("exit\n");
				os.flush();
				try {
					int suProcessRetval = suProcess.waitFor();
					if (255 != suProcessRetval) {
						retval = true;
					}
					else {
						retval = false;
					}
				}
				catch (Exception ex) {
					Log.w(TAG, "Error executing root action");
				}
			}
		}
		catch (IOException ex) {
			Log.w(TAG, "Can't get root access");
		}
		catch (SecurityException ex) {
			Log.w(TAG, "Can't get root access");
		}
		catch (Exception ex) {
			Log.w(TAG, "Error executing internal operation");
		}

		commands.clear();
		return retval;
	}
	
	public static final Boolean remountReadWrite(String path) {
		return RootTools.remount(path, "RW");
	}
	
	public static final Boolean remountReadOnly(String path) {
		return RootTools.remount(path, "RO");
	}

	/*
	public static final Boolean wget(String urlStr, String localName) {
        BufferedInputStream bis = null;

        try {
            URL url = new URL(urlStr);

            URLConnection urlCon = url.openConnection();
            bis = new BufferedInputStream(urlCon.getInputStream());

            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current = 0;
            while (((current = bis.read()) != -1)) {
                baf.append((byte) current);
                if (isCancelled()) {
                    return false;
                }
            }
            bis.close();

            if (isCancelled()) {
                return false;
            } else {
                FileOutputStream outFileStream = getActivity().openFileOutput(localName, 0);
                outFileStream.write(baf.toByteArray());
                outFileStream.close();
                if (localName.equals("busybox")) {
                    mBusyboxPath = getActivity().getFilesDir().getAbsolutePath().concat("/busybox");
                }
            }

        } catch (MalformedURLException e) {
            Log.e(TAG, "Bad URL: " + urlStr, e);
            return false;
        } catch (IOException e) {
            Log.e(TAG, "Problem downloading file: " + localName, e);
            return false;
        }
        return true;
    }
	
	public static final String md5sum(String s) {
	    try {
	        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
	        digest.update(s.getBytes());
	        byte messageDigest[] = digest.digest();
	 
	        StringBuffer hexString = new StringBuffer();
	        for (int i = 0; i < messageDigest.length; i++) {
	            String h = Integer.toHexString(0xFF & messageDigest[i]);
	            while (h.length() < 2)
	                h = "0" + h;
	            hexString.append(h);
	        }
	        return hexString.toString();
	 
	    }
	    catch (NoSuchAlgorithmException e) {
	        Log.e(TAG, "md5sum(): " + e.getMessage());
	    }
	    return "";
	}

	public static Boolean md5chk(String path, String md5sum) {
        if (mBusyboxPath == null) {
            Log.e(TAG, "Busybox not present");
            return false;
        }
        
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[] { mBusyboxPath, "md5sum", path});
            BufferedReader is = new BufferedReader(new InputStreamReader(new DataInputStream(process.getInputStream())), 64);
            BufferedReader es = new BufferedReader(new InputStreamReader(new DataInputStream(process.getErrorStream())), 64);
            for (int i = 0; i < 200; i++) {
                if (is.ready())
                	break;
                try {
                    Thread.sleep(5);
                }
                catch (InterruptedException e) {
                    Log.e(TAG, "md5chk(): sleep timer got interrupted");
                }
            }
            String inLine = null;
            if (es.ready()) {
                inLine = es.readLine();
                Log.i(TAG, inLine);
            }
            if (is.ready()) {
                inLine = is.readLine();
            }
            else {
                Log.e(TAG, "md5chk(): could not check md5sum");
                return false;
            }
            process.destroy();
            if (!inLine.split(" ")[0].equals(md5sum)) {
                Log.e(TAG, "md5chk(): checksum mismatch");
                return false;
            }
        } catch (IOException e) {
            Log.e(TAG, "md5chk(): checking of md5sum failed", e);
            return false;
        }
        return true;
    }
    
	public static String which(String binaryName) {
        String path = System.getenv("PATH");
        for (String s : path.split(File.pathSeparator)) {
            File file = new File(s, binaryName);
            if (file.exists()) {
                return file.getAbsolutePath();
            }
        }
        return null;
    }
	*/
}
