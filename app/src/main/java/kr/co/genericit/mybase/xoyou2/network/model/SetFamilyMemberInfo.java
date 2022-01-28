package kr.co.genericit.mybase.xoyou2.network.model;

import java.util.ArrayList;

import kr.co.genericit.mybase.xoyou2.network.response.FamilyMemberResult;

public class SetFamilyMemberInfo {
    ArrayList<FamilyMemberResult.Data.FamilyInfo> family;
    ArrayList<FamilyMemberResult.Data.FamilyInfo> fatherFamily;
    ArrayList<FamilyMemberResult.Data.FamilyInfo> motherFamily;
    ArrayList<FamilyMemberResult.Data.FamilyInfo> afterMarry;
    ArrayList<FamilyMemberResult.Data.FamilyInfo> job;

    public SetFamilyMemberInfo(ArrayList<FamilyMemberResult.Data.FamilyInfo> family,
                               ArrayList<FamilyMemberResult.Data.FamilyInfo> fatherFamily,
                               ArrayList<FamilyMemberResult.Data.FamilyInfo> motherFamily,
                               ArrayList<FamilyMemberResult.Data.FamilyInfo> afterMarry,
                               ArrayList<FamilyMemberResult.Data.FamilyInfo> job) {
        this.family = family;
        this.fatherFamily = fatherFamily;
        this.motherFamily = motherFamily;
        this.afterMarry = afterMarry;
        this.job = job;
    }




}


