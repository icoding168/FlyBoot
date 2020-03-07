package xyz.icoding168.flyboot.controller;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import xyz.icoding168.flyboot.common.helper.HttpHelper;
import xyz.icoding168.flyboot.common.helper.IdGenerator;
import xyz.icoding168.flyboot.service.UserService;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("storage")
public class StorageController {

    private static final Logger logger = LogManager.getLogger();

    @Autowired
    private UserService userService;

    @Value("${baseUrl}")
    private String baseUrl;

    private File rootFolder = new File("/tmp/attach-files/");

    @PostConstruct
    public void init(){
        if(rootFolder.exists() == false){
            rootFolder.mkdir();
        }
    }

    @RequestMapping("upload")
    public void upload(@RequestParam("upload") MultipartFile upload, ModelMap modelMap, HttpServletResponse response)
        throws Exception {

        String id = IdGenerator.generateId();

        File file = new File( rootFolder.getAbsolutePath() + "/" + id + "." +  FilenameUtils.getExtension(upload.getOriginalFilename()));

        upload.transferTo(file);

        Map map = new HashMap<>();

        map.put("uploaded",true);
        map.put("url",baseUrl + "/storage/" + file.getName());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().println(HttpHelper.getJsonString(map));

    }

    @RequestMapping("{fileName}")
    public void visit(@PathVariable("fileName")String fileName,HttpServletResponse response) throws Exception {
        File file = new File( rootFolder.getAbsolutePath() + "/" + fileName);

        InputStream in = new FileInputStream(file);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(in, response.getOutputStream());

        in.close();


    }
}
