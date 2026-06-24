package com.nexoservice.web.controller;

import com.nexoservice.cliente.dto.request.ClienteCreateRequest;
import com.nexoservice.cliente.dto.request.ClienteEstadoRequest;
import com.nexoservice.cliente.dto.request.ClienteUpdateRequest;
import com.nexoservice.cliente.dto.response.ClienteResponse;
import com.nexoservice.cliente.entity.TipoDocumento;
import com.nexoservice.cliente.service.ClienteService;
import com.nexoservice.web.form.ClienteForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteWebController {

    private final ClienteService clienteService;

    @GetMapping
    public String listar(@RequestParam(required = false) String buscar, Model model) {
        Pageable pageable = PageRequest.of(0, 50);
        model.addAttribute("clientes", clienteService.listar(buscar, pageable).getContent());
        model.addAttribute("buscar", buscar);
        return "clientes/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("cliente", new ClienteForm());
        model.addAttribute("tiposDocumento", TipoDocumento.values());
        return "clientes/form";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("cliente") ClienteForm form) {
        clienteService.crear(new ClienteCreateRequest(
                form.getNombres(),
                form.getApellidos(),
                form.getTipoDocumento(),
                form.getNumeroDocumento(),
                form.getCorreo(),
                form.getTelefono(),
                form.getDireccion()
        ));
        return "redirect:/clientes";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        ClienteResponse response = clienteService.buscarPorId(id);
        model.addAttribute("cliente", toForm(response));
        model.addAttribute("clienteId", id);
        model.addAttribute("tiposDocumento", TipoDocumento.values());
        return "clientes/form";
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id, @Valid @ModelAttribute("cliente") ClienteForm form) {
        clienteService.actualizar(id, new ClienteUpdateRequest(
                form.getNombres(),
                form.getApellidos(),
                form.getTipoDocumento(),
                form.getNumeroDocumento(),
                form.getCorreo(),
                form.getTelefono(),
                form.getDireccion()
        ));
        return "redirect:/clientes";
    }

    @PostMapping("/{id}/desactivar")
    public String desactivar(@PathVariable Long id) {
        clienteService.cambiarEstado(id, new ClienteEstadoRequest(false));
        return "redirect:/clientes";
    }

    private ClienteForm toForm(ClienteResponse response) {
        ClienteForm form = new ClienteForm();
        form.setNombres(response.nombres());
        form.setApellidos(response.apellidos());
        form.setTipoDocumento(response.tipoDocumento());
        form.setNumeroDocumento(response.numeroDocumento());
        form.setCorreo(response.correo());
        form.setTelefono(response.telefono());
        form.setDireccion(response.direccion());
        return form;
    }
}
