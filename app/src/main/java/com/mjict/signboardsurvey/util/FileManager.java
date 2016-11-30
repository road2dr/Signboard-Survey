package com.mjict.signboardsurvey.util;

import android.content.Context;
import android.mediautil.image.jpeg.LLJTran;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class FileManager {
	public static final String SD_CARD = "sdCard";
    public static final String EXTERNAL_SD_CARD = "externalSdCard";

    /**
     * @return True if the external storage is available. False otherwise.
     */
    public static boolean isAvailable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static String getSdCardPath() {
        return Environment.getExternalStorageDirectory().getPath() + "/";
    }

    /**
     * @return True if the external storage is writable. False otherwise.
     */
    public static boolean isWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;

    }

    /**
     * @return A map of all storage locations available
     */
    public static Map<String, File> getAllStorageLocations() {
        Map<String, File> map = new HashMap<String, File>(10);

        List<String> mMounts = new ArrayList<String>(10);
        List<String> mVold = new ArrayList<String>(10);
        mMounts.add("/mnt/sdcard");
        mVold.add("/mnt/sdcard");

        try {
            File mountFile = new File("/proc/mounts");
            if(mountFile.exists()){
                Scanner scanner = new Scanner(mountFile);
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    if (line.startsWith("/dev/block/vold/")) {
                        String[] lineElements = line.split(" ");
                        String element = lineElements[1];

                        // don't add the default mount path
                        // it's already in the list.
                        if (!element.equals("/mnt/sdcard"))
                            mMounts.add(element);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            File voldFile = new File("/system/etc/vold.fstab");
            if(voldFile.exists()){
                Scanner scanner = new Scanner(voldFile);
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    if (line.startsWith("dev_mount")) {
                        String[] lineElements = line.split(" ");
                        String element = lineElements[2];

                        if (element.contains(":"))
                            element = element.substring(0, element.indexOf(":"));
                        if (!element.equals("/mnt/sdcard"))
                            mVold.add(element);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        for (int i = 0; i < mMounts.size(); i++) {
            String mount = mMounts.get(i);
            if (!mVold.contains(mount))
                mMounts.remove(i--);
        }
        mVold.clear();

        List<String> mountHash = new ArrayList<String>(10);

        for(String mount : mMounts){
            File root = new File(mount);
            if (root.exists() && root.isDirectory() && root.canWrite()) {
                File[] list = root.listFiles();
                String hash = "[";
                if(list!=null){
                    for(File f : list){
                        hash += f.getName().hashCode()+":"+f.length()+", ";
                    }
                }
                hash += "]";
                if(!mountHash.contains(hash)){
                    String key = SD_CARD + "_" + map.size();
                    if (map.size() == 0) {
                        key = SD_CARD;
                    } else if (map.size() == 1) {
                        key = EXTERNAL_SD_CARD;
                    }
                    mountHash.add(hash);
                    map.put(key, root);
                }
            }
        }

        mMounts.clear();

        if(map.isEmpty()){
                 map.put(SD_CARD, Environment.getExternalStorageDirectory());
        }
        return map;
    }
    
//    Map<String, File> externalLocations = ExternalStorage.getAllStorageLocations();
//    File sdCard = externalLocations.get(ExternalStorage.SD_CARD);
//    File externalSdCard = externalLocations.get(ExternalStorage.EXTERNAL_SD_CARD);
    
    public static String getExternalSDPath(String dir) {
    	Map<String, File> externalLocations = getAllStorageLocations();
    	File externalSdCard = externalLocations.get(SD_CARD);
    	
    	return externalSdCard.getAbsolutePath()+"/"+dir;
    }
    
//    public static String get

	public static boolean copyDbFileTo(Context context, String objectPath) throws FileNotFoundException, SdNotMountedException, IOException {
		String state= Environment.getExternalStorageState();
		if(state.equals(Environment.MEDIA_MOUNTED)) {
//			Log.d("junseo", "����Ʈ��");
		} else {
			throw new SdNotMountedException("SD Card not mounted");
		}

		String dbName = SyncConfiguration.getDatabaseFileName();

		File targetFile = context.getDatabasePath(dbName);
		if(targetFile.exists() == false)
			throw new FileNotFoundException("file not found: "+targetFile);
	
		File of = new File(objectPath);
		if(of.exists() == false)
			of.createNewFile();
		
		InputStream is = new FileInputStream(targetFile);
		OutputStream os = new FileOutputStream(of);
		
		byte[] buf = new byte[1024];
		int len;
		while((len = is.read(buf))>0)
			os.write(buf, 0, len);
		
		os.flush();
		os.close();
		is.close();

		return true;
	}
	
	public static boolean copySyncDBFileToInternal(Context context) throws FileNotFoundException, SdNotMountedException, IOException {
		String state= Environment.getExternalStorageState(); //�ܺ������(SDcard)�� ���� ������
		if(state.equals(Environment.MEDIA_MOUNTED))
			Log.d("junseo", "마운트 됨");
		else {
			throw new SdNotMountedException("SD Card not mounted");
		}
		
		String targetPath = SyncConfiguration.getDatabaseFileNameForSync();
//		File sdPath = Environment.getExternalStorageDirectory();
		File targetFile = new File(targetPath);
		File objectFile = context.getDatabasePath(SyncConfiguration.getDatabaseFileName());
		
		if(targetFile.exists()==false)
			throw new FileNotFoundException("file not found: "+targetFile);
		
//		objectFile.
//		if(objectFile.g)
//		if(objectFile.exists()==false)
//			objectFile.createNewFile();
		
		InputStream is = new FileInputStream(targetFile);
		OutputStream os = new FileOutputStream(objectFile);
		
		byte[] buf = new byte[1024];
		int len;
		while((len = is.read(buf))>0)
			os.write(buf, 0, len);
		
		os.flush();
		os.close();
		is.close();
		
		return true;
	}
	

	
//	public static boolean writeDataToFile(byte[] data, String filePath) {
//		try {
//			FileOutputStream fos = new FileOutputStream(filePath);
//			try {
//				fos.write(data);
//				fos.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//				return false;
//			}
//			System.gc();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			return false;
//		}
//		
//		return true;
//	}
	
	public static boolean makeAndRoateJpg(byte[] data, String filePath, boolean rotate) {
		File file = new File(filePath);
		String fileName = file.getName();
		
//		String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		String directory = SyncConfiguration.getTempDirectory();
		String tempFileName = directory+fileName;
		File tempFile = new File(tempFileName);
		
		File picDirFile = new File(directory);
		if(picDirFile.exists() == false)
			picDirFile.mkdirs();
		
		// �ϴ� �ӽ÷� temp ������ ����
		try {
			FileOutputStream fos = new FileOutputStream(tempFile);
			fos.write(data);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		int degree = rotate ? 90 : 0;
		boolean answer =  rotateJpegFile(tempFile, file, degree);		
		tempFile.delete();
		
		return answer;
	}
	
	public static boolean rotateJpegFile(File imageFile, File outFile, int degree){
	try {

		int operation = 0;
//		int degree = 90;
//		//int degree = 90;
		switch(degree){
		case 90:operation = LLJTran.ROT_90;break;
		case 180:operation = LLJTran.ROT_180;break;
		case 270:operation = LLJTran.ROT_270;break;
		}
//		
//		if (operation == 0){
//			Log.d("junseo", "Image orientation is already correct");
//			return false;
//		}

		OutputStream output = null;
		LLJTran llj = null;
		try {   
			// Transform image
			llj = new LLJTran(imageFile);
			llj.read(LLJTran.READ_ALL, false); //don't know why setting second param to true will throw exception...
			
			if(operation != 0)	// junseo
				llj.transform(operation, LLJTran.OPT_DEFAULTS | LLJTran.OPT_XFORM_ORIENTATION);

			// write out file
			output = new BufferedOutputStream(new FileOutputStream(outFile));
			llj.save(output, LLJTran.OPT_WRITE_ALL);
			return true;
		} catch(Exception e){
			e.printStackTrace();
			return false;
		}finally {
			if(output != null)output.close();
			if(llj != null)llj.freeMemory();
		}
	} catch (Exception e) {
		// Unable to rotate image based on EXIF data
		e.printStackTrace();
		return false;
	}
}
	
	
	
//	public static boolean rotateJpegFileBaseOnExifWithLLJTran(File imageFile, File outFile){
//		try {
//
//			int operation = 0;
//			int degree = getExifRotateDegree(imageFile.getAbsolutePath());
//			//int degree = 90;
//			switch(degree){
//			case 90:operation = LLJTran.ROT_90;break;
//			case 180:operation = LLJTran.ROT_180;break;
//			case 270:operation = LLJTran.ROT_270;break;
//			}
//			
////			if (operation == 0){
////				Log.d("junseo", "Image orientation is already correct");
////				return false;
////			}
//
//			OutputStream output = null;
//			LLJTran llj = null;
//			try {   
//				// Transform image
//				llj = new LLJTran(imageFile);
//				llj.read(LLJTran.READ_ALL, false); //don't know why setting second param to true will throw exception...
//				
//				if(operation != 0)	// junseo
//					llj.transform(operation, LLJTran.OPT_DEFAULTS | LLJTran.OPT_XFORM_ORIENTATION);
//
//				// write out file
//				output = new BufferedOutputStream(new FileOutputStream(outFile));
//				llj.save(output, LLJTran.OPT_WRITE_ALL);
//				return true;
//			} catch(Exception e){
//				e.printStackTrace();
//				return false;
//			}finally {
//				if(output != null)output.close();
//				if(llj != null)llj.freeMemory();
//			}
//		} catch (Exception e) {
//			// Unable to rotate image based on EXIF data
//			e.printStackTrace();
//			return false;
//		}
//	}
//
//	public static int getExifRotateDegree(String imagePath){
//		try {
//			ExifInterface exif;
//			exif = new ExifInterface(imagePath);
//			String orientstring = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
//			int orientation = orientstring != null ? Integer.parseInt(orientstring) : ExifInterface.ORIENTATION_NORMAL;
//			if(orientation == ExifInterface.ORIENTATION_ROTATE_90) 
//				return 90;
//			if(orientation == ExifInterface.ORIENTATION_ROTATE_180) 
//				return 180;
//			if(orientation == ExifInterface.ORIENTATION_ROTATE_270) 
//				return 270;
//		} catch (IOException e) {
//			e.printStackTrace();
//		}       
//		return 0;
//	}
}
