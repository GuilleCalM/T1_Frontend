package pe.edu.cibertec.t1_frontend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import pe.edu.cibertec.t1_frontend.dto.AutoRequestDTO;
import pe.edu.cibertec.t1_frontend.dto.AutoResponseDTO;
import pe.edu.cibertec.t1_frontend.viewmodel.AutoModel;

@Controller
@RequestMapping("/auto")
public class AutoController {
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/index")
    public String home(Model model) {
        AutoModel autoModel = new AutoModel("00","","","","","","");
        model.addAttribute("autoModel", autoModel);
        return "index";
    }

    @PostMapping("/buscarPlaca")
    public String buscarPlaca(@RequestParam("placa") String placa, Model model){
        if(placa == null || placa.trim().isEmpty()){
            AutoModel autoModel = new AutoModel("02","Debe ingresar una placa","","","","","");
            model.addAttribute("autoModel", autoModel);
            return "index";
        }else if(!placa.matches("[A-Za-z0-9-]{8}")){
            AutoModel autoModel = new AutoModel("02","Debe ingresar una placa válida (8 caracteres)","","","","","");
            model.addAttribute("autoModel", autoModel);
            return "index";
        }else{
            try{
                String api = "http://localhost:8081/auto/verificarplaca";
                AutoRequestDTO request = new AutoRequestDTO(placa);
                AutoResponseDTO response = restTemplate.postForObject(api,request,AutoResponseDTO.class);

                if(response.codigo().equals("00")){
                    AutoModel autoModel = new AutoModel("00","",
                            response.marca(),response.modelo(),response.nroAsientos(),response.precio(),response.color());
                    model.addAttribute("autoModel", autoModel);
                    return "main";
                }else{
                    AutoModel autoModel = new AutoModel("01","No se encontró un vehículo para la placa ingresada","","","","","");
                    model.addAttribute("autoModel", autoModel);
                    return "index";
                }
            }catch (Exception e){
                AutoModel autoModel = new AutoModel("99","Ocurrió un error al procesar su solicitud","","","","","");
                model.addAttribute("autoModel", autoModel);
                return "index";
            }
        }
    }
}
