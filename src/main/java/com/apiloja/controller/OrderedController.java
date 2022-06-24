package com.apiloja.controller;

import java.util.List;

import javax.validation.Valid;

import com.apiloja.dto.ordereddto.OrderedDTO;

import com.apiloja.service.ordered.OrderedService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin()
@Tag(name = "Pedidos")
@RequestMapping(value = "/ordered/")
public class OrderedController {

	OrderedService orderedService;


	public OrderedController(OrderedService orderedService) {

		this.orderedService = orderedService;

	}
	@Operation(summary = "Listar do usuário", description = "Lista pedidos do usuário logado.")
	@GetMapping("/user/")
	public List<OrderedDTO> orderedOfUser() {

		return orderedService.orderedOfUser();
	}


	@Operation(summary = "Listar todos pedidos", description = "Lista todos os pedidos feitos por usuários. Somente administrador")
	@GetMapping("/all/")
	public ResponseEntity<Page<OrderedDTO>> orderedAll(@PageableDefault(page = 0, size = 10, sort = "date")
														Pageable page) {

			return new ResponseEntity<>(orderedService.orderedAll(page), HttpStatus.OK);

	}

	@Operation(summary = "Buscar pedido", description = "Busca pedido for email de usuário. Somente administrador")
	@GetMapping("/find/{email}/")
	public ResponseEntity<Page<OrderedDTO>> findOrderedByEmailUser(
			@SortDefault(sort = "date", direction = Sort.Direction.DESC)
			@PageableDefault(page = 0, size = 10) Pageable page
			,@PathVariable("email") String email
	) {

			return new ResponseEntity<>(orderedService.findOrderedByEmailUser(page, email), HttpStatus.OK);

	}

	@Operation(summary = "Salvar pedido", description = "Salvar pedido feito por usuário.")
	@PostMapping("/")
	public ResponseEntity<Boolean> postOrdered(@Valid @RequestBody OrderedDTO orderedDTO) {

		if(orderedService.postOrdered(orderedDTO))
			return ResponseEntity.status(HttpStatus.CREATED).body(true);
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);

	}

	@Operation(summary = "Editar pedido", description = "Edita pedido de usuário. Somente administrador")
	@PutMapping("/{id}")
	public ResponseEntity<Boolean> orderedPut(@PathVariable Long id, @RequestBody String status) {

		if(orderedService.orderedPut(id, status))
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(true);
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);

	}

	@Operation(summary = "Deletar pedido", description = "Deleta pedido de usuário. Somente administrador")
	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> deleteOrdered(@PathVariable Long id) {

		if(orderedService.deleteOrdered(id))
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(true);
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
	}


}
