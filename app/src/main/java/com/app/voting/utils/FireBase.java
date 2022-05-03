package com.app.voting.utils;

import static com.app.voting.utils.Const.PARTIES;
import static com.app.voting.utils.Const.USERS;
import static com.app.voting.utils.Const.VOTING;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FireBase {


    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public static DatabaseReference USER_REF = FirebaseDatabase.getInstance().getReference(VOTING).child(USERS);
    public static DatabaseReference PARTY_REF = FirebaseDatabase.getInstance().getReference(VOTING).child(PARTIES);
    public static final String UID = firebaseAuth.getUid();

}
