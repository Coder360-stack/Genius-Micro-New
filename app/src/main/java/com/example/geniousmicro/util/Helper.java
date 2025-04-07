package com.example.geniousmicro.util;

import com.example.geniousmicro.Models.UtilityModels.MenuItemModel;
import com.example.geniousmicro.R;

import java.util.ArrayList;

public class Helper {
    public static void AddMenuList() {
        Constants.menuItemList = new ArrayList<>();


       //Constants.menuItemList.add(new MenuItemModel("All Collection", R.drawable.investment_report));

        Constants.menuItemList.add(new MenuItemModel("New Member", R.drawable.new_member));
        Constants.menuItemList.add(new MenuItemModel("Policy Renewal", R.drawable.policy_renewal));
        Constants.menuItemList.add(new MenuItemModel("Loan EMI", R.drawable.loan_emi));
        Constants.menuItemList.add(new MenuItemModel("Loan Details", R.drawable.loan_details));
        Constants.menuItemList.add(new MenuItemModel("Policy Details", R.drawable.policy_details));
        Constants.menuItemList.add(new MenuItemModel("Approved Member List", R.drawable.loan_due_report));
        Constants.menuItemList.add(new MenuItemModel("UnApproved Member List", R.drawable.policy_details));
        Constants.menuItemList.add(new MenuItemModel("LoanDue Report", R.drawable.investment_report));
        Constants.menuItemList.add(new MenuItemModel("LS Transaction", R.drawable.plan_details));
        Constants.menuItemList.add(new MenuItemModel("Loan Collection Report", R.drawable.plan_details));
      // Constants.menuItemList.add(new MenuItemModel("Policy Renewal", R.drawable.policy_renewal));
       // Constants.menuItemList.add(new MenuItemModel("Today LoanDue Coll", R.drawable.plan_details));
        //Constants.menuItemList.add(new MenuItemModel("Today PolicyDue Coll", R.drawable.plan_details));
        Constants.menuItemList.add(new MenuItemModel("LS Transaction Report", R.drawable.test1));
        //Constants.menuItemList.add(new MenuItemModel("LoanCollection", R.drawable.loan_due_report));
        Constants.menuItemList.add(new MenuItemModel("Policy Collection Report", R.drawable.investment_report));
        Constants.menuItemList.add(new MenuItemModel("All Collection", R.drawable.loan_emi));
        Constants.menuItemList.add(new MenuItemModel("Today LoanCollection", R.drawable.investment_report));
        Constants.menuItemList.add(new MenuItemModel("Today PolicyCollection", R.drawable.investment_report));
        Constants.menuItemList.add(new MenuItemModel("Group Loan Collection", R.drawable.loan_due_report));
        Constants.menuItemList.add(new MenuItemModel("Group Collection Report", R.drawable.investment_report));
        Constants.menuItemList.add(new MenuItemModel("LoanCollection Manual", R.drawable.investment_report));
        Constants.menuItemList.add(new MenuItemModel("PolicyCollection Manual", R.drawable.investment_report));


    }



    public static void getCheckedMenu(String permissionedMenu) {
        for (MenuItemModel model : Constants.menuItemList) {
            if (model.getTitle().equals(permissionedMenu)) {
                Constants.submenuItemList.add( model);
            }
        }
    }

}
