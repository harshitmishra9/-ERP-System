package com.example.Erp.Project.Service;

import java.util.List;

import com.example.Erp.Project.Entity.GRN;
import com.example.Erp.Project.Entity.GRNItem;

public interface GRNService {

	 GRN createGRN(Long purchaseOrderId, List<GRNItem> grnItems);

	    List<GRN> getAllGRNs();
}
