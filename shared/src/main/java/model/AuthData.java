package model;

import com.google.gson.*;

/**
 * Field	Type
 * --------------
 * authToken	String
 * username	String
 */

public record AuthData(String authToken, String username){};