/*
 * Copyright 2005-2007 WSO2, Inc. (http://wso2.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.usermanager.readwrite;

public class DefaultRealmConstants {

    public static final int ADD_USER = 1;
    public static final int ADD_ROLE = 2;
    public static final int ADD_USER_ROLE = 3;
    public static final int ADD_USER_ATTRIBUTE = 4;
    public static final int ADD_ROLE_ATTRIBUTE = 5;
    public static final int ADD_PERMISSION = 6;
    public static final int ADD_ROLE_PERMISSION = 7;
    public static final int ADD_USER_PERMISSION = 8;
    public static final int UPDATE_USER = 9;

    public static final int DELETE_USER = 20;
    public static final int DELETE_ROLE = 21;
    public static final int DELETE_USER_ROLE = 22;
    public static final int DELETE_ROLE_ATTRIBUTE = 23;
    public static final int DELETE_USER_ATTRIBUTE = 24;
    public static final int DELETE_PERMISSION_ON_RESOURCE = 25;
    public static final int GET_USER = 26;
    public static final int GET_ROLE = 27;
    public static final int DELETE_USER_PERMISSION = 28;
    public static final int DELETE_ROLE_PERMISSION = 29;

    public static final int GET_USER_ROLES = 41;
    public static final int GET_ROLE_ATTRIBUTES = 42;
    public static final int GET_USER_ATTRIBUTES = 43;
    public static final int GET_PERMISSION = 44;
    public static final int GET_ROLE_AUTHORIZED = 45;
    public static final int GET_USER_AUTHORIZED = 46;
    public static final int GET_ALLOWED_ROLES_FOR_RESOURCE = 47;
    public static final int GET_DENIED_ROLES_FOR_RESOURCE = 48;
    public static final int GET_ALLOWED_USERS_ON_RESOURCE = 49;
    public static final int GET_DENIED_USERS_ON_RESOURCE = 50;
    public static final int GET_ROLE_PERMISSION = 51;
    public static final int GET_USER_PERMISSION = 52;
    public static final int GET_ROLES_ALL = 53;
    public static final int GET_USERS_ALL = 54;
    public static final int GET_USER_ID = 55;
    public static final int GET_ROLE_ID = 56;
    public static final int GET_RESOURCE_PERMISSION = 57;
    public static final int GET_ATTRIBUTE_NAMES = 58;
    public static final int GET_USERS_IN_ROLE = 59;
    public static final int GET_USERS_WITH_PROPERTY = 60;
    public static final int GET_USERS_WITH_PROPERTY_VALUE = 61;

    public static final int COLUMN_ID_ATTR_NAME = 1;
    public static final int COLUMN_ID_ATTR_VALUE = 2;
    public static final int COLUMN_ID_ID = 3;
    public static final int COLUMN_ID_USER_NAME = 4;
    public static final int COLUMN_ID_ROLE_NAME = 5;
    public static final int COLUMN_ID_ROLE_ID = 6;
    public static final int COLUMN_ID_USER_ID = 7;
    public static final int COLUMN_ID_IS_ALLOWED = 8;
    public static final int COLUMN_ID_CREDENTIAL = 9;
    public static final int COLUMN_ID_ACTION = 10;

    public static final String ADD_USER_SQL = "insert into um_users (user_name, password, id) values (?, ?, ?)";
    public static final String ADD_ROLE_SQL = "insert into um_roles (role_name, id) values (?, ?)";
    public static final String ADD_USER_ROLE_SQL = "insert into um_user_roles (id, user_id, role_id) values (?, ?, ?)";
    public static final String ADD_USER_ATTRIBUTE_SQL = "insert into um_user_attributes (attr_name, attr_value, user_id, id) values (?, ?, ?, ?)";
    public static final String ADD_ROLE_ATTRIBUTE_SQL = "insert into um_role_attributes (attr_name, attr_value, role_id, id) values (?, ?, ?, ?)";
    public static final String ADD_PERMISSION_SQL = "insert into um_permissions (resource_id, action, id) values (?, ?, ?)";
    public static final String ADD_ROLE_PERMISSION_SQL = "insert into um_role_permissions (permission_id, is_allowed, role_id, id) values (?, ?, ?, ?)";
    public static final String ADD_USER_PERMISSION_SQL = "insert into um_user_permissions (permission_id, is_allowed, user_id, id) values (?, ?, ?, ?)";
    public static final String UPDATE_USER_SQL = "update um_users set password= ? where user_name= ?";
    public static final String DELETE_USER_SQL = "delete from um_users where user_name = ?";
    public static final String DELETE_ROLE_SQL = "delete from um_roles where role_name = ?";

    public static final String DELETE_USER_ROLE_SQL = "delete from um_user_roles"
            + " where user_id=(select id from um_users where user_name=?)"
            + " and role_id=(select id from um_roles where role_name=?)";

    public static final String DELETE_ROLE_ATTRIBUTE_SQL = "delete from um_role_attributes"
            + " where role_id = ?";

    public static final String DELETE_USER_ATTRIBUTE_SQL = "delete from um_user_attributes"
            + " where user_id = ?";

    public static final String DELETE_PERMISSION_ON_RESOURCE_SQL = "delete from um_permissions"
            + " where resource_id = ?";

    public static final String GET_USER_SQL = "select user_name, password from um_users where user_name=?";
    public static final String GET_ROLE_SQL = "select * from um_roles where role_name=?";

    public static final String DELETE_USER_PERMISSION_SQL = "delete from um_user_permissions"
            + " where user_id = ? and permission_id = ?";

    public static final String DELETE_ROLE_PERMISSION_SQL = "delete from um_role_permissions"
            + " where role_id = ? and permission_id = ?";

    public static final String GET_USER_ROLES_SQL = "select um_roles.role_name"
            + " from um_user_roles, um_roles, um_users where um_users.user_name=?"
            + " and um_users.id=um_user_roles.user_id and"
            + " um_roles.id=um_user_roles.role_id";

    public static final String GET_ROLE_ATTRIBUTES_SQL = "select *"
            + " from um_role_attributes, um_roles"
            + " where um_roles.id = um_role_attributes.role_id"
            + " and um_roles.role_name=?";

    public static final String GET_USER_ATTRIBUTES_SQL = "select *"
            + " from um_user_attributes, um_users"
            + " where um_users.id = um_user_attributes.user_id"
            + " and um_users.user_name=?";

    public static final String GET_PERMISSION_SQL = "select id"
            + " from um_permissions where um_permissions.resource_id=?"
            + " and um_permissions.action=?";

    public static final String GET_ROLE_AUTHORIZED_SQL = "select"
            + " um_role_permissions.is_allowed"
            + " from um_role_permissions, um_permissions, um_roles"
            + " where um_role_permissions.role_id=um_roles.id"
            + " and um_role_permissions.permission_id=um_permissions.id"
            + " and um_permissions.resource_id=?"
            + " and um_permissions.action=? and um_roles.role_name=?";

    public static final String GET_USER_AUTHORIZED_SQL = "select um_user_permissions.is_allowed"
            + " from um_user_permissions, um_permissions, um_users"
            + " where um_user_permissions.user_id=um_users.id"
            + " and um_user_permissions.permission_id=um_permissions.id"
            + " and um_permissions.resource_id=?"
            + " and um_permissions.action=?" + " and um_users.user_name=?";

    public static final String GET_ALLOWED_ROLES_FOR_RESOURCE_SQL = "select um_roles.role_name"
            + " from um_role_permissions, um_permissions, um_roles"
            + " where um_permissions.resource_id=?"
            + " and um_permissions.action=?"
            + " and um_permissions.id=um_role_permissions.permission_id"
            + " and um_role_permissions.role_id=um_roles.id"
            + " and um_role_permissions.is_allowed=1";

    public static final String GET_DENIED_ROLES_FOR_RESOURCE_SQL = "select um_roles.role_name"
            + " from um_role_permissions, um_permissions, um_roles"
            + " where um_permissions.resource_id=?"
            + " and um_permissions.action=?"
            + " and um_permissions.id=um_role_permissions.permission_id"
            + " and um_role_permissions.role_id=um_roles.id"
            + " and um_role_permissions.is_allowed=0";

    public static final String GET_ALLOWED_USERS_ON_RESOURCE_SQL = "select um_users.user_name"
            + " from um_user_permissions, um_permissions, um_users"
            + " where um_permissions.resource_id=?"
            + " and um_permissions.action=?"
            + " and um_permissions.id=um_user_permissions.permission_id"
            + " and um_user_permissions.user_id=um_users.id"
            + " and um_user_permissions.is_allowed=1";

    public static final String GET_DENIED_USERS_ON_RESOURCE_SQL = "select um_users.user_name"
            + " from um_user_permissions, um_permissions, um_users"
            + " where um_permissions.resource_id=?"
            + " and um_permissions.action=?"
            + " and um_permissions.id=um_user_permissions.permission_id"
            + " and um_user_permissions.user_id=um_users.id"
            + " and um_user_permissions.is_allowed=0";

    public static final String GET_ROLE_PERMISSION_SQL = "select * from um_role_permissions"
            + " where permission_id=?";
    public static final String GET_USER_PERMISSION_SQL = "select * from um_user_permissions"
            + " where permission_id=?";
    public static final String GET_ROLES_ALL_SQL = "select role_name from um_roles";
    public static final String GET_USERS_ALL_SQL = "select user_name from um_users";
    public static final String GET_USER_ID_SQL = "select id from um_users where user_name=?";
    public static final String GET_ROLE_ID_SQL = "select id from um_roles where role_name=?";

    public static final String GET_RESOURCE_PERMISSION_SQL = "select * from um_permissions"
            + " where um_permissions.resource_id=?";
    public static final String GET_ATTRIBUTE_NAMES_SQL = "select distinct attr_name"
            + " from  um_user_attributes";

    public static final String GET_USERS_IN_ROLE_SQL = "select um_users.user_name"
            + " from um_users, um_user_roles, um_roles"
            + " where um_users.id=um_user_roles.user_id"
            + " and um_roles.id=um_user_roles.role_id"
            + " and um_roles.role_name=?";

    public static final String GET_USERS_WITH_PROPERTY_SQL = "select user_name"
            + " from um_users, um_user_attributes where um_users.id =um_user_attributes.user_id"
            + " and um_user_attributes.attr_name=? and um_user_attributes.attr_value=?";

    public static final String GET_USERS_WITH_PROPERTY_VALUE_SQL = "select user_name"
            + " from um_users, um_user_attributes where um_users.id =um_user_attributes.user_id"
            + " and um_user_attributes.attr_value=?";

    public static final String COLUMN_NAME_ATTR_NAME = "attr_name";
    public static final String COLUMN_NAME_ATTR_VALUE = "attr_value";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_USER_NAME = "user_name";
    public static final String COLUMN_NAME_ROLE_NAME = "role_name";
    public static final String COLUMN_NAME_ROLE_ID = "role_id";
    public static final String COLUMN_NAME_USER_ID = "user_id";
    public static final String COLUMN_NAME_IS_ALLOWED = "is_allowed";
    public static final String COLUMN_NAME_CREDENTIAL = "password";
    public static final String COLUMN_NAME_ACTION = "action";

}
