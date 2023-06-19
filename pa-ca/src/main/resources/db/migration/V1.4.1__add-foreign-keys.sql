--Date: 2023-02-08

-- foreign keys
-- Reference: business_tier (table: business)
ALTER TABLE business DROP CONSTRAINT business_tier;
ALTER TABLE business ADD CONSTRAINT business_tier
    FOREIGN KEY (tier_id)
    REFERENCES tier (id)  
    ON DELETE CASCADE
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: client_group_client (table: client_group)
ALTER TABLE client_group ADD CONSTRAINT client_group_client
    FOREIGN KEY (client_id)
    REFERENCES client (id)  
    ON DELETE CASCADE
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: client_user (table: client)
ALTER TABLE client ADD CONSTRAINT client_user
    FOREIGN KEY (user_id)
    REFERENCES "user" (id)  
    ON DELETE CASCADE
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: favorite_sede_client (table: favorite_branch)
ALTER TABLE favorite_branch ADD CONSTRAINT favorite_sede_client
    FOREIGN KEY (client_id)
    REFERENCES client (id)  
    ON DELETE CASCADE
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: favorite_sede_sede (table: favorite_branch)
ALTER TABLE favorite_branch ADD CONSTRAINT favorite_sede_sede
    FOREIGN KEY (branch_id)
    REFERENCES branch (id)  
    ON DELETE CASCADE
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: friend_client_addressee (table: friend)
ALTER TABLE friend ADD CONSTRAINT friend_client_addresser
    FOREIGN KEY (client_addresser_id)
    REFERENCES client (id)  
    ON DELETE CASCADE
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: friend_client_requester (table: friend)
ALTER TABLE friend ADD CONSTRAINT friend_client_requester
    FOREIGN KEY (client_requester_id)
    REFERENCES client (id)  
    ON DELETE CASCADE
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: invoice_reservation (table: invoice)
ALTER TABLE invoice ADD CONSTRAINT invoice_reservation
    FOREIGN KEY (reservation_id)
    REFERENCES reservation (id)  
    ON DELETE CASCADE
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: product_product_category (table: product)
ALTER TABLE product ADD CONSTRAINT product_product_sub_category
    FOREIGN KEY (product_sub_category_id)
    REFERENCES product_sub_category (id)  
    ON DELETE CASCADE
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: product_sub_category_product_category (table: product_sub_category)
ALTER TABLE product_sub_category ADD CONSTRAINT product_sub_category_product_category
    FOREIGN KEY (product_category_id)
    REFERENCES product_category (id)  
    ON DELETE CASCADE
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: product_sub_category_branch (table: product_sub_category)
ALTER TABLE product_sub_category ADD CONSTRAINT product_sub_category_branch
    FOREIGN KEY (branch_id)
    REFERENCES branch (id)  
    ON DELETE CASCADE
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: reservation_client_group (table: client_group)
ALTER TABLE client_group ADD CONSTRAINT reservation_client_group
    FOREIGN KEY (reservation_id)
    REFERENCES reservation (id)  
    ON DELETE CASCADE
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: reservation_sede (table: reservation)
ALTER TABLE reservation ADD CONSTRAINT reservation_sede
    FOREIGN KEY (branch_id)
    REFERENCES branch (id)  
    ON DELETE CASCADE
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: review_client (table: review)
ALTER TABLE review ADD CONSTRAINT review_client
    FOREIGN KEY (client_id)
    REFERENCES client (id)  
    ON DELETE CASCADE
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: review_list_client (table: review_like)
ALTER TABLE review_like ADD CONSTRAINT review_list_client
    FOREIGN KEY (client_id)
    REFERENCES client (id)  
    ON DELETE CASCADE
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: review_list_review (table: review_like)
ALTER TABLE review_like ADD CONSTRAINT review_list_review
    FOREIGN KEY (review_id)
    REFERENCES review (id)  
    ON DELETE CASCADE
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: review_sede (table: review)
ALTER TABLE review ADD CONSTRAINT review_sede
    FOREIGN KEY (branch_id)
    REFERENCES branch (id)  
    ON DELETE CASCADE
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: role_user (table: user)
ALTER TABLE "user" ADD CONSTRAINT role_user
    FOREIGN KEY (role_id)
    REFERENCES role (id)  
    ON DELETE CASCADE
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: sede_business (table: branch)
ALTER TABLE branch ADD CONSTRAINT sede_business
    FOREIGN KEY (business_id)
    REFERENCES business (id)  
    ON DELETE CASCADE
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: sede_preferences_list_sede (table: branch_amenity)
ALTER TABLE branch_amenity ADD CONSTRAINT sede_preferences_list_sede
    FOREIGN KEY (branch_id)
    REFERENCES branch (id)  
    ON DELETE CASCADE
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: sede_preferences_list_sede_preferences (table: branch_amenity)
ALTER TABLE branch_amenity ADD CONSTRAINT sede_preferences_list_sede_preferences
    FOREIGN KEY (amenity_id)
    REFERENCES amenity (id)  
    ON DELETE CASCADE
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: sede_promotion (table: promotion)
ALTER TABLE promotion ADD CONSTRAINT sede_promotion
    FOREIGN KEY (branch_id)
    REFERENCES branch (id)  
    ON DELETE CASCADE
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: user_business (table: business)
ALTER TABLE business ADD CONSTRAINT user_business
    FOREIGN KEY (user_id)
    REFERENCES "user" (id)  
    ON DELETE CASCADE
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- End of file.

