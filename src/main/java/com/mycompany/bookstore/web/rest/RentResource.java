package com.mycompany.bookstore.web.rest;

import com.mycompany.bookstore.service.RentService;
import com.mycompany.bookstore.service.dto.RentDTO;
import com.mycompany.bookstore.web.rest.vm.RentBookVm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
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

    public RentResource(RentService rentService) {
        this.rentService = rentService;
    }

    @PostMapping("/rents")
    public ResponseEntity<RentDTO> createRent(@RequestBody RentBookVm rentBookVm) {
        RentDTO rent = rentService.createRent(rentBookVm.getIsbn());
        if (rent.getBookIsbn() == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(rent, HttpStatus.CREATED);
    }

    @GetMapping("/rents")
    public ResponseEntity<List<RentDTO>> getAllActiveRents(Pageable pageable) {
        final Page<RentDTO> page = rentService.getAllActiveRents(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
