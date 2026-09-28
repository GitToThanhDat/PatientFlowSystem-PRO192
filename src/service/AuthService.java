/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;
import java.util.List;
import java.util.ArrayList;
import model.Admin;
import model.Doctor;
import model.Receptionist;
import model.User;
import model.Role;
import util.TextUtils;

/**
 *
 * @author ACER
 */
public class AuthService {
    private List<User> _userList;
    private int _idCounter;
    
    public AuthService(){
        this._userList= new ArrayList<>() ;
        this._idCounter=1;
    }
    
    public User findById(String id){
        for (User u : this._userList){
            if(u.getId().equals(id)) return u;
        }
        return null;
    }
    
    public String register(
            Role role, 
            String userName, 
            String password, 
            String fullName){
        
        if (role ==null || userName ==null || password == null || fullName == null) return null;
        
        for (User u: this._userList){
            if (u.getUserName().equalsIgnoreCase(userName)){
                System.out.println("This username is already used.");
                return null;
            }
        }
        String id = "USR-" + TextUtils.cleanName(fullName) +"-"+ _idCounter++;
        switch (role){
            case DOCTOR:
                Doctor doctor = new Doctor(id, userName, password, fullName, false, null);
                this._userList.add(doctor);
                break;
            case RECEPTIONIST:
                Receptionist recep  = new Receptionist(id, userName, password, fullName, false);
                this._userList.add(recep);
                break;
            case ADMIN:
                Admin ad =new Admin(id, userName, password, fullName, false);
                this._userList.add(ad);
                break;
        }
        System.out.println("Register successfully and your own id is: "+id); //Đồng bộ thông báo và gỡ bỏ việc thông báo cho cả team.
        return id;
    }
    
    public User login(String userName, String password){ //Hàmm này sẽ trả về null còn việc thông báo trong exception sẽ là username hoặc password sai chứ không chỉ rõ.
        if (userName == null || password == null) return null;
        for(User u : this._userList){
            if (u.getUserName().equals(userName) && u.getPassword().equals(password)){
                u.setActivate(true);
                return u;
            }
        }
        return null;    
    }
    public boolean updateAccountFullName(String id, String newFullName){
        User u = findById(id);
        if (u!=null) {
            u.setFullName(newFullName);
            return true;
        }
        return false;
    }
    
    public boolean deActivateAccount(String id){
        User u= findById(id);
        if (u!=null){
            u.setActivate(false);
            return true;
        }
        return false;
    }
    
    public List<User> getAll(){
        return this._userList;
    }
}
