package com.mycompany.bookstore.web.rest;

import com.mycompany.bookstore.service.RentService;
import com.mycompany.bookstore.service.dto.RentDTO;
import com.mycompany.bookstore.web.rest.vm.RentBookVm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;

import java.util.List;

/**
 * Rest controller for managing rents
 */

@RestController
@RequestMapping("/api")
public class RentResource {

    private final Logger log = LoggerFactory.getLogger(RentResource.class);
    private final RentService rentService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public RentResource(RentService rentService) {
        this.rentService = rentService;
    }


    /**
     * {@code POST  /api/rents}  : Creates a rent
     *
     * @param rentBookVm The book to be rented.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the created rent,
     * or with status {@code 409 (Conflict)} when rent was not created,
     * or with status {@code 400 (Bad Request) if the user already has 3 rents active}.
     */

    @PostMapping("/rents")
    public ResponseEntity<RentDTO> createRent(@RequestBody RentBookVm rentBookVm) {
        if (!rentService.canUserRentBook()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createAlert(applicationName, "User can not make more than 3 rents", rentBookVm.getIsbn())).build();
        }
        RentDTO rent = rentService.createRent(rentBookVm.getIsbn());
        if (rent.getBookIsbn() == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(rent, HttpStatus.CREATED);
    }


    /**
     * {@code GET  /api/rents}  : Gets a list of all the rents
     *
     * @param pageable the information of the pages and size of the pages .
     * @return the {@link ResponseEntity} with status {@code 200 (ok)} and with a list of rents.
     */

    @GetMapping("/rents")
    public ResponseEntity<List<RentDTO>> getAllActiveRents(Pageable pageable) {
        final Page<RentDTO> page = rentService.getAllActiveRents(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
