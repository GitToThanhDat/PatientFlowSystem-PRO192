/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exception;

/**
 *
 * @author ACER
 */
public class AuthenticationException extends Exception {
    public AuthenticationException(String message){
        super(message); // thằng super này sẽ lấy thông tin message này để sau này thông qua class Exception để sử dụng các hàm khác nó sẽ hiển thị thông tin lỗi đúng như ý mình muốn.
    }
}
