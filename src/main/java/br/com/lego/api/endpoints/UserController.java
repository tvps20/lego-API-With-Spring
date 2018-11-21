package br.com.lego.api.endpoints;

import br.com.lego.api.erros.ResourceNotFoundException;
import br.com.lego.api.models.User;
import br.com.lego.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    // Métodos Crud
    @GetMapping
    public ResponseEntity<?> listAll() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUsersById(@PathVariable("id") Long id) {
        verifyIfUserExists(id);
        return new ResponseEntity<Optional>(userRepository.findById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody User user) {
        return new ResponseEntity<>(userRepository.save(user), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        verifyIfUserExists(id);
        userRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody User user) {
        verifyIfUserExists(user.getId());
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    // Métodos Auxiliares
    private void verifyIfUserExists(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User not fount for ID: " + id);
        }
    }
}
