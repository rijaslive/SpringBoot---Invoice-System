package com.bolsadeideas.springboot.app.controllers;

import javax.servlet.http.HttpServletRequest;

import com.bolsadeideas.springboot.app.dto.ResponseDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HealthController {

	//Este método sirve para ser llamado por los botones de cambiar de idioma de
	//la barra de navegación. De esta manera, los otros parámetros que pudiese
	//haber en la URL se mantienen, ya que si se añade diréctamente el parámetro
	//?lang=idioma con un href, el resto de parámetros se eliminan
	@GetMapping(value = "/__health", produces = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity<ResponseDto> locale(HttpServletRequest request) {
		return ResponseEntity.ok(ResponseDto.builder().stataus("success").code("200").response("Running fine....!").build());
	}

}
