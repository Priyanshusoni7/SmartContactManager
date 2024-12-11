package com.ctm.contactManager.entities;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class contact {

    @Id
    private String Id;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    @Column(length = 1000)
    private String picture;
    @Column(length = 1000)
    private String description;
    private boolean favorite = false; 

    private String websiteLink;
    private String Linkedin;
    //private List<String> socialLinks = new ArrayList<>();

    private String cloudinaryImagePublicId;

    @ManyToOne
    @JsonIgnore
    private user user;

    @OneToMany(mappedBy = "contact",cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Social_Link> socialLinks = new ArrayList<>();
}
