package com.ada.avanadestore.constants;

public class ErrorMessages {

    public static final String NAME_CANNOT_BE_EMPTY_NULL = "Field cannot be empty or null: name";
    public static final String EMAIL_CANNOT_BE_NULL = "Field cannot be null: email";
    public static final String PASSWORD_CANNOT_BE_NULL = "Field cannot be null: password";
    public static final String PASSWORD_MIN_LENGTH_8 = "Field password must be at least 8 characters";
    public static final String CPF_CANNOT_BE_NULL = "Field cannot be null: cpf";
    public static final String BIRTHDATE_CANNOT_BE_NULL = "Field cannot be null: birthdate";
    public static final String INVALID_CPF_FORMAT = "Invalid cpf. Correct format is xxx.xxx.xxx-xx";
    public static final String INVALID_EMAIL_FORMAT = "Invalid email. Correct format is user@mailprovider.com";
    public static final String ADDRESS_CANNOT_BE_NULL = "Field cannot be null: address";
    public static final String STREET_CANNOT_BE_NULL = "Field cannot be null: street";
    public static final String NUMBER_CANNOT_BE_NULL = "Field cannot be null: number";
    public static final String CITY_CANNOT_BE_NULL = "Field cannot be null: city";
    public static final String STATE_CANNOT_BE_NULL = "Field cannot be null: state";
    public static final String ZIP_CANNOT_BE_NULL = "Field cannot be null: zip";
    public static final String STREET_SIZE_EXCEEDED = "Field street should not exceed 80 characters";
    public static final String NUMBER_SIZE_EXCEEDED = "Field number should not exceed 10 characters";
    public static final String CITY_SIZE_EXCEEDED = "Field city should not exceed 60 characters";
    public static final String STATE_SIZE_EXCEEDED = "Field state should not exceed 60 characters";
    public static final String ZIP_SIZE_EXCEEDED = "Field zip should not exceed 9 characters";
    public static final String ZIP_INVALID_FORMAT = "Invalid zip format. Correct format is 00000-000";
    public static final String USER_NOT_FOUND = "User could not be found";
    public static final String AUTHENTICATION_INVALID = "Authentication failed. JWT could be invalid or untrustworthy";
    public static final String INVALID_CREDENTIALS = "Invalid credentials: Username or password";
    public static final String PRODUCT_NOT_FOUND = "Product could not be found";
    public static final String USER_NOT_NULL = "Field cannot be null: user";
    public static final String ORDER_ITEMS_NOT_NULL = "Field cannot be null: orderItems";
    public static final String ORDER_ITEMS_NOT_EMPTY = "Field cannot be empty: orderItems";
    public static final String PRODUCT_NOT_NULL = "Field cannot be null: product";
    public static final String QUANTITY_NOT_NULL = "Field cannot be empty: quantity";
    public static final String QUANTITY_MIN_VALUE = "Field cannot be low than 1: quantity";
    public static final String ORDER_CANCELLED = "Requested order cannot update because it's cancelled";
    public static final String ORDER_COMPLETED = "Requested order cannot update because it's completed";
    public static final String ORDER_NOT_FOUND = "Order could not be found";
    public static final String EXCEED_PRODUCT_STOCK = "Order exceed products quantity";

}
