package com.ctm.contactManager.entities;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Social_Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    private String link;
    private String title;


    @ManyToOne
    private contact contact;
}
