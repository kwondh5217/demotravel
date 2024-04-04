package org.ssafy.demotravel.accounts;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/accounts")
public class AccountController {

    private final AccountService accountService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity createAccount(@Valid @RequestBody AccountJoinRequestDto accountJoinRequestDto,
                                        Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().build();
        }
        Account savedAccount;
        try {
            savedAccount = accountService.save(modelMapper.map(accountJoinRequestDto, Account.class));
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(modelMapper.map(savedAccount, AccountResponseDto.class));
    }

}
