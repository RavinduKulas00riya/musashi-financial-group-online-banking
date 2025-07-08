package lk.jiat.app.web.security;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;
import lk.jiat.app.core.model.User;
import lk.jiat.app.core.model.UserType;
import lk.jiat.app.core.service.UserService;

import java.util.Set;

@ApplicationScoped
public class AppIdentityStore implements IdentityStore {

    @EJB
    private UserService userService;

    @Override
    public CredentialValidationResult validate(Credential credential) {
        if (credential instanceof UsernamePasswordCredential) {
            UsernamePasswordCredential upc = (UsernamePasswordCredential) credential;

            User user = userService.validate(upc.getCaller(), upc.getPasswordAsString());

            System.out.println(user);

            if (user != null && user.getUserType().equals(UserType.ADMIN)) {

                return new CredentialValidationResult(user.getEmail(), Set.of(UserType.ADMIN.name()));

            }

        }

        System.out.println("Invalid result");
        return CredentialValidationResult.INVALID_RESULT;
    }
}
