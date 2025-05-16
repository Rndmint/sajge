package io.github.sajge.messages.resquests;

public enum RequestType {
    ECHO,

    SIGNUP,
    LOGIN,
    LOGOUT,
    DELETE_ACCOUNT,

    OPEN_PROJECT_SESS,
    RENDER_SCENE,
    EDIT_SCENE,
    CREATE_SCENE,
    INVITE_USER,

    SCENE_CREATED,
    CREATE_PROJECT,
    USER_INVITED,

    BATCH,
    FETCH_PINGS

}
