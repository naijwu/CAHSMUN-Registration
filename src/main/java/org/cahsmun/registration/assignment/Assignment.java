package org.cahsmun.registration.assignment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.cahsmun.registration.delegate.Delegate;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name="assignment", schema="public")
public class Assignment implements Serializable {

    @Id
    @Column(name="assignment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long assignment_id;

    @Column(name="country")
    private String country;

    @Column(name="committee")
    private String committee;

    /*
    TODO: figure out after displaying country matrix
    @Column(name="delegate", insertable=false, updatable=false)
    private Delegate delegate;
    */

    @JoinColumn(name = "delegate_id", nullable = false)
    @JsonIgnore
    private Delegate delegate;
}
