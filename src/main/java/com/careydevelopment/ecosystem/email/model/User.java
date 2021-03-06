package com.careydevelopment.ecosystem.email.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "#{@environment.getProperty('mongo.user.collection')}")
public class User implements UserDetails {

	private static final long serialVersionUID = 3592549577903104696L;

	private String id;
	
	private String firstName;
	private String lastName;
	private String street1;
	private String street2;
	private String city;
	private String state;
	private String zip;
	private String email;
	private String phoneNumber;
	
	@JsonIgnore
	private List<String> authorityNames = new ArrayList<String>();
	
	private String username;
	private String country;
	
	@JsonIgnore
	private String password;	

	@JsonIgnore
    private GoogleApi googleApi;

	private EmailIntegration emailIntegration;

	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getStreet1() {
		return street1;
	}
	public void setStreet1(String street1) {
		this.street1 = street1;
	}
	public String getStreet2() {
		return street2;
	}
	public void setStreet2(String street2) {
		this.street2 = street2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	

	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getAuthorityNames() {
		return authorityNames;
	}
	public void setAuthorityNames(List<String> authorityNames) {
		this.authorityNames = authorityNames;
	}
	
	public GoogleApi getGoogleApi() {
        return googleApi;
    }
    public void setGoogleApi(GoogleApi googleApi) {
        this.googleApi = googleApi;
    }
    
    public EmailIntegration getEmailIntegration() {
        return emailIntegration;
    }
    public void setEmailIntegration(EmailIntegration emailIntegration) {
        this.emailIntegration = emailIntegration;
    }
    @Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}
	
	
	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}
	
	
	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	
	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return true;
	}
	
	
	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> list = authorityNames
				.stream()
				.map(auth -> new SimpleGrantedAuthority(auth))
				.collect(Collectors.toList());

		return list;
	}
	
	
	public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
	
	
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
