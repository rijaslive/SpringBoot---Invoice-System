package com.bolsadeideas.springboot.app.controllers;

import com.bolsadeideas.springboot.app.dto.CashBookDto;
import com.bolsadeideas.springboot.app.dto.ResponseDto;
import com.bolsadeideas.springboot.app.models.entity.CashBook;
import com.bolsadeideas.springboot.app.models.entity.Client;
import com.bolsadeideas.springboot.app.models.entity.TransactionMode;
import com.bolsadeideas.springboot.app.models.entity.TransactionType;
import com.bolsadeideas.springboot.app.models.service.IClientService;
import com.bolsadeideas.springboot.app.models.service.IUploadFileService;
import com.bolsadeideas.springboot.app.service.CashBookService;
import com.bolsadeideas.springboot.app.util.DateUtil;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;


@Controller
@SessionAttributes("cashbookfilter")
@Slf4j
public class CashBookFilterController {


	@Autowired
	private MessageSource messageSource;

	@Autowired
	private CashBookService cashBookService;
	
	


	@Secured("ROLE_ADMIN")
	@RequestMapping(value={"/cashbookfilter"}, method=RequestMethod.GET)
	public String loadCashbookListPage(Model model,
										Authentication authentication,
										HttpServletRequest request) {

		List<TransactionMode> transactionModes = cashBookService.findAllTransactionModes();
		List<TransactionType> transactionTypes = cashBookService.findAllTransactionTypes();
		model.addAttribute("title", "Cashbook List");
		model.addAttribute("transactionTypes", transactionTypes);
		model.addAttribute("transactionModes", transactionModes);
		return "/cashbook_filter";
	}

	@Secured("ROLE_ADMIN")
	@PostMapping(value={"/cashbookfilter/list"})
	public ResponseEntity<ResponseDto> loadCashbookList(@RequestBody Map<String,Object> requestObject) {
		Map<String, Object> results = cashBookService.fetchAllCashBooksByFilter(requestObject);
		ResponseDto response = ResponseDto.builder()
				.code("200")
				.stataus("success")
				.response(results)
				.build();
		return ResponseEntity.ok(response);
	}


	private boolean hasRole(String role) {
		SecurityContext context = SecurityContextHolder.getContext();
		if(context != null) {
			Authentication auth = context.getAuthentication();
			if(auth != null) {
				Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
				/*for(GrantedAuthority authority : authorities) {
					if(role.equals(authority.getAuthority())) {
						return true;
					}
				}*/
				return authorities.contains(new SimpleGrantedAuthority(role));	//Esta forma es más escueta que utilizando el for
			}
		}
		return false;
	}
}
