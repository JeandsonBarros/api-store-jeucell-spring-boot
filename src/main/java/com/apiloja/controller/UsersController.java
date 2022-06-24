package com.apiloja.controller;

import com.apiloja.dto.usertdto.ForgotPasswordDTO;
import com.apiloja.dto.usertdto.PasswordChangeDTO;
import com.apiloja.dto.productdto.ProductDTO;
import com.apiloja.dto.usertdto.UserShoppingDTO;

import com.apiloja.service.user.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;


@RestController
@CrossOrigin()
@Tag(name = "Usuários da Loja")
@RequestMapping(value = "/users/")
public class UsersController {


	UsersService usersService;

	public UsersController(UsersService usersService) {

		this.usersService = usersService;

	}

	@Operation(summary = "Lista de usuários", description = "Retorna paginas contendo listas de todos usuários, apenas para administrador.")
	@GetMapping("/all/")
	public ResponseEntity<Page<UserShoppingDTO>> getAllUsers(@PageableDefault(page = 0, size = 10) Pageable page) {

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(usersService.getAllUsers(page));

	}

	@Operation(summary = "Dados do usuário logado.", description = "Retorna dados do usuário logado no momento.")
	@GetMapping("/authenticated")
	public ResponseEntity<UserShoppingDTO> getUserAuthenticated() {

		return ResponseEntity.status(HttpStatus.OK).body(usersService.getUserAuthenticated());
	}

	@Operation(summary = "Lista de usuário por busca.", description = "Retorna paginas de contendo usuários que incluem o email especificado, apenas para administrador.")
	@GetMapping("/{email}")
	public ResponseEntity<Page<UserShoppingDTO>> findUserByEmail(@PageableDefault(page = 0, size = 10) Pageable page, @PathVariable("email") String email) {

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(usersService.findUserByEmail(page, email));
	}

	@Operation(summary = "Lista de produtos favoritos do usuário.", description = "Retorna lista de produtos favoritos do usuário logado.")
	@GetMapping("/favorites")
	public List<ProductDTO> getFavorites() {
		return usersService.getFavorites();
	}

	@Operation(summary = "Altera lista de produtos favoritos do usuário.", description = "Adiciona ou remove produto da lista de produtos favoritos do usuário logado.")
	@PutMapping("/favorites")
	public void putFavorites(@Valid @RequestBody ProductDTO productDTO) {

		usersService.putFavorites(productDTO);
	}

	@Operation(summary = "Cadastrar usuário.", description = "Cadastra novo usuário da loja. ")
	@PostMapping("/")
	public ResponseEntity<String> postUser(@Valid @RequestBody UserShoppingDTO userDTO) {

		return ResponseEntity.status(HttpStatus.OK).body(usersService.postUser(userDTO));

	}

	@Operation(summary = "Enviar e-mail para alterar senha.", description = "Envia um e-mail contendo o código para alterar a senha. ")
	@PostMapping("/send-email")
	public ResponseEntity<String> sendEmail(@Valid @RequestBody String email) {


		if(usersService.sendEmail(email))
			return ResponseEntity.status(HttpStatus.OK).body("E-mail enviado");
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não encontrado");
	}

	@Operation(summary = "Verificar senha.", description = "Verifica se o código passo pelo usuário é valido. ")
	@GetMapping("/check-code/{code}")
	public ResponseEntity<String> checkCode(@Valid @PathVariable int code){
		if(usersService.checkCode(code))
			return ResponseEntity.status(HttpStatus.OK).body("Código aceito");
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Código inválido ou expirado");

	}



	@Operation(summary = "Editar usuário.", description = "Edita usuário da loja. ")
	@PutMapping("/{email}")
	public ResponseEntity<Boolean> editUser(@Valid @RequestBody UserShoppingDTO userShoppingDTO, @PathVariable String email) {

		if(usersService.editUser(userShoppingDTO, email))
			return ResponseEntity.status(HttpStatus.OK).body(true);
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);

	}

	@Operation(summary = "Alterar a senha esquecida.", description = "Altera a senha do usuário,  recebe como parametro o código, e-mail e a nova senha. ")
	@PutMapping("/forgotpassword/")
	public ResponseEntity<String> forgotPassword(
		@Valid @RequestBody ForgotPasswordDTO forgotPasswordDTO
	)
	{
		if(usersService.forgotPassword(forgotPasswordDTO))
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Senha alterada.");
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao alterar senha.");


	}

	@Operation(summary = "Alter senha.", description = "Altera senha do usuário da loja. ")
	@PutMapping("/changepasswor/{email}/")
	public ResponseEntity<String> changePassword(@Valid @RequestBody PasswordChangeDTO passwordChangeDTO, @PathVariable String email) {

		if(usersService.changePassword(passwordChangeDTO, email))
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Senha alterada.");
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao alterar senha.");

	}

	@Operation(summary = "Deleta usuário.", description = "Deleta usuário da loja, apenas para administrador. ")
	@DeleteMapping("/{email}")
	public ResponseEntity<Boolean> deleteUser(@PathVariable String email) {

		if(usersService.deleteUser(email))
			return ResponseEntity.status(HttpStatus.OK).body(true);
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);

	}



}
