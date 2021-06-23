package com.unravel.scout.model.entity.v1;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "recently_viewed")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "created_at", "updated_at","item"})
public class RecentlyViewed extends BaseEntity  {

	  	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @NotNull
	    private String userId;

	    @ManyToOne(fetch = FetchType.LAZY, optional = false)
	    @JoinColumn(name = "item_id", nullable = false)
	    @OnDelete(action = OnDeleteAction.CASCADE)
	    private ItemDetail item;

}
