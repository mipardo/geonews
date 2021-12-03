package es.uji.geonews.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.uji.geonews.model.data.Data;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceHttp;
import es.uji.geonews.model.services.ServiceName;

public class ServiceWrapper {

    protected static Map<String, ServiceDao> convertServices(Map<ServiceName, Service> servicesMap) {
        Map<String, ServiceDao> resultMap = new HashMap<>();
        ServiceDao serviceDao;
        for (Service service : servicesMap.values()) {
            serviceDao = new ServiceDao();
            serviceDao.setServiceName(service.getServiceName().name);
            serviceDao.setActivationDate(service.getActivationDate());
            serviceDao.setActive(service.isActive());
            resultMap.put(serviceDao.getServiceName(), serviceDao);
        }
        return resultMap;
    }

}