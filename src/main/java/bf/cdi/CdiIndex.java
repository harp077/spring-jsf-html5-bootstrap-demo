package bf.cdi;

import bf.model.HashDataModel;
//import com.google.common.base.Charsets;
//import com.google.common.hash.Hashing;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
//import org.primefaces.event.FileUploadEvent;
//import org.primefaces.model.UploadedFile;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.annotation.SessionScope;

@Named
//@RequestScope
@SessionScope
public class CdiIndex {

    public CdiIndex() {
    }
    
    @Inject 
    private CdiMessages cdiMess;        

    private String hashText;
    //private Map<String, String> hashes = new HashMap<>();
    private List<HashDataModel> textHashesList = new ArrayList<>();
    private List<HashDataModel> fileHashesList = new ArrayList<>();
    private HashDataModel hdm;
    private Part uploadedFile;
    private File targetFile;
    private String fileName;
    private byte[] fileContents;    
    
    private String[] TYPES = {
        "md2",
        "md5",
        "sha1",
        "sha256"
        //,"sha384"
        //,"sha512"//,
        /////////
        /*"crc32",
        "crc32c",
        "adler32",
        "farmHashFingerprint64",
        "murmur3_128",
        "murmur3_32",
        "sipHash24",
        "goodFastHash32"*/
    };

    @PostConstruct
    public void afterBirn() {
        textHashesList.clear();
        fileHashesList.clear();
    }

    public void calcTextHashesAll() {
        if (!hashText.isEmpty()) {
            textHashesList.clear();
            for (String tip: TYPES) {
                hdm=new HashDataModel();
                hdm.setType(tip);
                switch(tip) {
                    case "md2": hdm.setHash(DigestUtils.md2Hex(hashText)); break;
                    //case "md5": hdm.setHash(Hashing.md5().hashString(hashText, Charsets.UTF_8).toString()); break;
                    case "md5": hdm.setHash(DigestUtils.md5Hex(hashText)); break;
                    case "sha1": hdm.setHash(DigestUtils.sha1Hex(hashText)); break;
                    case "sha256": hdm.setHash(DigestUtils.sha256Hex(hashText)); break;
                    case "sha384": hdm.setHash(DigestUtils.sha384Hex(hashText)); break;
                    //case "sha512": hdm.setHash(DigestUtils.sha512Hex(hashText)); break;
                    //case "crc32": hdm.setHash(Hex.encodeHexString(Hashing.crc32().hashUnencodedChars(hashText).toString().getBytes())); break;
                    //case "crc32": hdm.setHash(Hashing.crc32().hashBytes((getBytesUtf8(hashText))).toString()); break;
                    /*case "crc32": hdm.setHash(Hashing.crc32().hashString(hashText, Charsets.UTF_8).toString()); break;
                    case "crc32c": hdm.setHash(Hashing.crc32c().hashString(hashText, Charsets.UTF_8).toString()); break;
                    case "adler32": hdm.setHash(Hashing.adler32().hashString(hashText, Charsets.UTF_8).toString()); break;
                    case "farmHashFingerprint64": hdm.setHash(Hashing.farmHashFingerprint64().hashString(hashText, Charsets.UTF_8).toString()); break;
                    case "murmur3_128": hdm.setHash(Hashing.murmur3_128().hashString(hashText, Charsets.UTF_8).toString()); break;
                    case "murmur3_32": hdm.setHash(Hashing.murmur3_32().hashString(hashText, Charsets.UTF_8).toString()); break;
                    case "sipHash24": hdm.setHash(Hashing.sipHash24().hashString(hashText, Charsets.UTF_8).toString()); break;
                    case "goodFastHash32": hdm.setHash(Hashing.goodFastHash(32).hashString(hashText, Charsets.UTF_8).toString()); break;*/
                }
                textHashesList.add(hdm);
                //System.out.println(textHashesList);
                
            }
            /*hashes.put("md2", DigestUtils.md2Hex(hashText));
            //////////  Hashing.crc32().hashString(buf, Charsets.UTF_8).toString()
            hashes.put("adler32",Hashing.adler32().hashUnencodedChars(hashText).toString());*/
            cdiMess.addMessage("Hashes calculated !","calc-hash",FacesMessage.SEVERITY_INFO);
        } else {cdiMess.addMessage("Emty Text Area !","null-text",FacesMessage.SEVERITY_INFO);}
    }
    
    //@Async
    public void upload() {
        /*if (file.getSize()<=0L) {
            cdiMess.addMessage("Empty file !","null-file",FacesMessage.SEVERITY_INFO);
            return;
        }*/
        try {
            fileHashesList.clear();
            targetFile = new File("/tmp/targetFile.tmp");
            FileUtils.copyInputStreamToFile(uploadedFile.getInputStream(), targetFile);
            fileContents=FileUtils.readFileToByteArray(targetFile);
            fileName=uploadedFile.getName();
            for (String tip: TYPES) {
                hdm=new HashDataModel();
                hdm.setType(tip);
                switch(tip) {
                    case "md2": hdm.setHash(DigestUtils.md2Hex(uploadedFile.getInputStream())); break;
                    case "md5": hdm.setHash(DigestUtils.md5Hex(uploadedFile.getInputStream())); break;
                    case "sha1": hdm.setHash(DigestUtils.sha1Hex(uploadedFile.getInputStream())); break;
                    case "sha256": hdm.setHash(DigestUtils.sha256Hex(uploadedFile.getInputStream())); break;
                    case "sha384": hdm.setHash(DigestUtils.sha384Hex(uploadedFile.getInputStream())); break;
                    //case "sha512": hdm.setHash(DigestUtils.sha512Hex(uploadedFile.getInputStream())); break;
                    //case "crc32": hdm.setHash(com.google.common.io.Files.asByteSource(targetFile).hash(Hashing.crc32()).toString()); break;
                    //case "crc32": hdm.setHash(com.google.common.io.Files.hash(targetFile, Hashing.crc32()).toString()); break;
                    //case "crc32c": hdm.setHash(com.google.common.io.Files.hash(targetFile, Hashing.crc32c()).toString()); break;
                    //case "adler32": hdm.setHash(com.google.common.io.Files.hash(targetFile, Hashing.adler32()).toString()); break;
                    //case "farmHashFingerprint64": hdm.setHash(com.google.common.io.Files.hash(targetFile, Hashing.farmHashFingerprint64()).toString()); break;
                    //case "murmur3_128": hdm.setHash(com.google.common.io.Files.hash(targetFile, Hashing.murmur3_128()).toString()); break;
                    //case "murmur3_32": hdm.setHash(com.google.common.io.Files.hash(targetFile, Hashing.murmur3_32()).toString()); break;
                    //case "sipHash24": hdm.setHash(com.google.common.io.Files.hash(targetFile, Hashing.sipHash24()).toString()); break;
                    //case "goodFastHash32": hdm.setHash(com.google.common.io.Files.hash(targetFile, Hashing.goodFastHash(32)).toString()); break;
                }
                fileHashesList.add(hdm);
                //System.out.println("file hash = " + DigestUtils.md5(fileContents));
            }
            targetFile.delete();
        } catch (IOException ex) {
            targetFile.delete();
            System.out.println(ex.toString());
            cdiMess.addMessage("IO-exception !","io-exception",FacesMessage.SEVERITY_INFO);
        }
    }  
    
    /*public void handleFileUpload(FileUploadEvent event) {
        this.file=event.getFile();
        if (file.getSize()>0L) {
            cdiMess.addMessage("Succesful"+event.getFile().getFileName()+" is uploaded !","load-file",FacesMessage.SEVERITY_INFO);
            upload();
        }     
    } */   
    
    public String getHashText() {
        return hashText;
    }

    public void setHashText(String hashText) {
        this.hashText = hashText;
    }

    public List<HashDataModel> getTextHashesList() {
        return textHashesList;
    }

    public List<HashDataModel> getFileHashesList() {
        return fileHashesList;
    }

    public Part getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(Part uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public String getFileName() {
        return fileName;
    }
    
}
