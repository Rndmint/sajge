package io.github.sajge.core.services;

import io.github.sajge.dao.SignupDao;

public class SignupService {

    private final SignupDao dao;

    public SignupService(SignupDao dao) {
        this.dao = dao;
    }

    public void create(String username, String email) throws Exception {
        dao.create(username, email);
    }

}
