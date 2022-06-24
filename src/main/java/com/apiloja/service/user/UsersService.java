package com.apiloja.service.user;

import com.apiloja.dto.usertdto.ForgotPasswordDTO;
import com.apiloja.dto.usertdto.PasswordChangeDTO;
import com.apiloja.dto.productdto.ProductDTO;
import com.apiloja.dto.usertdto.UserShoppingDTO;
import com.apiloja.model.products.Product;
import com.apiloja.model.user.UserShopping;
import com.apiloja.model.user.ChangePassword;

import com.apiloja.repository.ordered.OrderedRepository;
import com.apiloja.repository.products.EvaluationProductRepository;
import com.apiloja.repository.products.ProductOfCartRepository;
import com.apiloja.repository.products.ProductsRepository;
import com.apiloja.repository.user.ChangePasswordRepository;
import com.apiloja.repository.user.UsersRepository;
import org.springframework.beans.BeanUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class UsersService {

    ProductsRepository pR;
    UsersRepository uR;
    OrderedRepository oR;
    ProductOfCartRepository pCR;

    EvaluationProductRepository ePR;

    ChangePasswordRepository changePassword;

    public UsersService(EvaluationProductRepository ePR, ChangePasswordRepository changePassword, ProductsRepository pR, UsersRepository uR, OrderedRepository oR, ProductOfCartRepository pCR) {
        this.pR = pR;
        this.uR = uR;
        this.oR = oR;
        this.pCR = pCR;
        this.ePR = ePR;
        this.changePassword = changePassword;

    }

    public Page<UserShoppingDTO> getAllUsers(Pageable page) {

        Page<UserShopping> userShoppings = uR.findAll(page);
        List<UserShoppingDTO> userShoppingDTOS = new ArrayList<>();

        userShoppings.forEach(data ->{
            var userDTO = new UserShoppingDTO();
            BeanUtils.copyProperties(data, userDTO);
            userShoppingDTOS.add(userDTO);
        });

        return new PageImpl<>(userShoppingDTOS, page, userShoppings.getTotalElements());
    }

    public UserShoppingDTO getUserAuthenticated() {

        var user = new UserShoppingDTO();
        BeanUtils.copyProperties(getUser(), user);
        user.setPassword("");

        return user;
    }

    public Page<UserShoppingDTO> findUserByEmail(Pageable page, String email) {

        Page<UserShopping> userShoppings = uR.findByEmailContaining(email, page);
        List<UserShoppingDTO> userShoppingDTOS = new ArrayList<>();

        userShoppings.forEach(data ->{
            var userDTO = new UserShoppingDTO();
            BeanUtils.copyProperties(data, userDTO);
            userShoppingDTOS.add(userDTO);
        });

        return new PageImpl<>(userShoppingDTOS, page, userShoppings.getTotalElements());
    }

    public List<ProductDTO> getFavorites() {

        List<ProductDTO> productDTOS = new ArrayList<>();

        getUser().getFavoriteProducts().forEach(data ->{
            var productDTO = new ProductDTO();
            BeanUtils.copyProperties(data, productDTO);
            productDTOS.add(productDTO);
        });

        return productDTOS;
    }

    @Transactional
    public void putFavorites(ProductDTO productDTO) {
        UserShopping user = getUser();
        List<Product> products = user.getFavoriteProducts();

        boolean veri = false;
        int cont = 0;

        for (Product pro : products) {
            if (pro.getId() == productDTO.getId()) {
                veri = true;
                break;
            }
            cont++;
        }

        if (veri) {
            user.getFavoriteProducts().remove(cont);
        } else {
            var product = new Product();
            BeanUtils.copyProperties(productDTO, product);
            user.setFavoriteProducts(product);
        }
        uR.save(user);
    }

    @Transactional
    public String postUser(UserShoppingDTO userDTO) {

        if(uR.findById(userDTO.getEmail()).isPresent()){
            return "Usuário já existe";
        }else {
            var user = new UserShopping();

            BeanUtils.copyProperties(userDTO, user);

            user.setRole("USER");
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            uR.save(user);

            return "Usuário cadastrado";
        }


    }

    @Transactional
    public boolean editUser(UserShoppingDTO userDTO, String email) {
        if(
                uR.findById(email).isPresent()
                && new BCryptPasswordEncoder().matches(userDTO.getPassword(), getUser().getPassword())
                && userDTO.getEmail().equals(getUser().getEmail())
        ){

            var user = new UserShopping();
            BeanUtils.copyProperties(userDTO, user);

            user.setPassword(getUser().getEmail());
            user.setRole("USER");
            user.setPassword(getUser().getPassword());
            uR.save(user);

            BeanUtils.copyProperties(uR.save(user), userDTO);

            return true;

        }else {
            return false;
        }

    }

    @Autowired
    private JavaMailSender emailSender;

    @Transactional
    public boolean sendEmail(String email) {

        if(uR.findById(email).isPresent()) {
           // emailModel.setSendDateEmail(LocalDateTime.now());
            try{
                Random gerador = new Random();

                var code = 0;
                var checkCode = 0;

                while (checkCode == 0){
                    code = gerador.nextInt();
                    if (!changePassword.findById(code).isPresent()){
                        checkCode = 1;
                    }
                }

                var change = new ChangePassword();

                change.setCode(code);
                change.setEmail(email);

                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("example@gmail.com");
                message.setTo(email);
                message.setSubject("Código para alterar a senha da loja JeuCell");
                message.setText("Código: "+change.getCode());

                emailSender.send(message);
                change.setExpiration(System.currentTimeMillis() + 600000);
                changePassword.save(change);

                return true;

            } catch (MailException e){
                System.out.println(e);
                return false;
            }

        }
        else {
            return false;
        }
    }

    public boolean checkCode(int code){

        if(changePassword.findById(code).isPresent()){
            if (System.currentTimeMillis() < changePassword.findById(code).get().getExpiration()) {
                return true;
            }else {
                changePassword.deleteById(code);
                return false;
            }
        }
        else {

            return false;
        }
    }
    @Transactional
    public boolean forgotPassword(ForgotPasswordDTO forgotPasswordDTO){
        if(uR.findById(forgotPasswordDTO.getEmail()).isPresent() &&
            changePassword.findById(forgotPasswordDTO.getCode()).isPresent() &&
                changePassword.findById(forgotPasswordDTO.getCode()).get().getEmail().equals(forgotPasswordDTO.getEmail())
        ){
            var user = uR.findById(forgotPasswordDTO.getEmail()).get();
            user.setPassword(new BCryptPasswordEncoder().encode(forgotPasswordDTO.getConfirmNewPassword()));

            uR.save(user);

            changePassword.deleteAll(changePassword.findAll());

            return true;
        }
        else {
            return false;
        }
    }

    @Transactional
    public boolean changePassword(PasswordChangeDTO change, String email) {
        if(
                uR.findById(email).isPresent()
                        && new BCryptPasswordEncoder().matches(change.getOldPassword(), getUser().getPassword())
                        && uR.findById(email).get().getEmail().equals(getUser().getEmail())
        ){
           var changeUser = getUser();
           changeUser.setPassword(new BCryptPasswordEncoder().encode(change.getNewPassword()));
            uR.save(changeUser);
            return true;
        }else {
            return false;
        }



    }

    @Transactional
    public boolean deleteUser(String email) {

        if(uR.findById(email).isPresent()) {

            var user = uR.findById(email).get();

            ePR.deleteAll(ePR.findByUserEmail(email));
            oR.deleteAll(oR.findByUser(user));
            pCR.deleteAll(pCR.findByUserEmail(email));
            uR.delete(uR.findById(email).get());

            return true;

        }else {

            return false;

        }

    }

    private UserShopping getUser() {
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        UserShopping user = uR.findByEmail(auth.getName());

        return user;
    }
}
