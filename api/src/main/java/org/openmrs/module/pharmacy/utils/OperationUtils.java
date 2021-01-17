package org.openmrs.module.pharmacy.utils;

import com.mifmif.common.regex.Generex;
import org.openmrs.*;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.*;
import org.openmrs.module.pharmacy.api.*;
import org.openmrs.module.pharmacy.enumerations.Goal;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;
import org.openmrs.module.pharmacy.models.PharmacyDateRange;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class OperationUtils {
    public static Boolean validateOperation(ProductOperation operation) {
        return service().validateOperation(operation);
    }

    public static void emptyStock(Location location, ProductProgram productProgram) {
        List<ProductAttributeStock> stocks = stockService().getAllProductAttributeStocks(location, false);
        for (ProductAttributeStock stock : stocks) {
            if (productProgram.getProducts().contains(stock.getProductAttribute().getProduct())) {
                stock.setQuantityInStock(0);
                stockService().saveProductAttributeStock(stock);
            }
        }
    }

    public static Boolean cancelOperation(ProductOperation operation) {
        return service().cancelOperation(operation);
    }

    private static PharmacyService service() {
        return Context.getService(PharmacyService.class);
    }

    private static ProductProgramService programService() {
        return Context.getService(ProductProgramService.class);
    }

    private static ProductAttributeFluxService fluxService() {
        return Context.getService(ProductAttributeFluxService.class);
    }

    private static ProductReportService reportService() {
        return Context.getService(ProductReportService.class);
    }

    private static ProductAttributeStockService stockService() {
        return Context.getService(ProductAttributeStockService.class);
    }

    public static Location getUserLocation() {
        if (Context.getUserContext().getLocation() != null) {
            return Context.getUserContext().getLocation();
        }
        return Context.getLocationService().getDefaultLocation();
    }

    public static List<Location> getUserLocations() {
        List<Location> locations = new ArrayList<>();
        locations.add(getUserLocation());
        locations.addAll(getUserLocation().getChildLocations());
        return locations;
    }

    public static List<ProductProgram> getUserLocationPrograms() {
        return getLocationPrograms(getUserLocation());
    }

    public static List<Location> getUserChildLocationsByProgram(ProductProgram productProgram) {
        return getChildLocationsByProgram(getUserLocation(), productProgram);
    }

    public static List<Location> getChildLocationsByProgram(Location location, ProductProgram productProgram) {
        List<Location> childLocations = new ArrayList<>();
        for (Location childLocation : location.getChildLocations()) {
            if (getLocationPrograms(childLocation).contains(productProgram)) {
                childLocations.add(childLocation);
            }
        }
        return childLocations;
    }
    public static List<ProductProgram> getLocationPrograms(Location location) {
        List<ProductProgram> productPrograms = new ArrayList<>();
        for (LocationAttribute attribute : location.getActiveAttributes()) {
            if (attribute.getAttributeType().getName().equals("Programmes Disponibles")) {
                String programString = attribute.getValue().toString();
                if (programString != null) {
                    String[] programsString = programString.split(",");
                    for (String programName : programsString) {
                       productPrograms.add(programService().getOneProductProgramByName(programName));
                    }
                }
                break;
            }
        }
        return productPrograms;
    }

    public static <T> List<T> getLastElements(final Iterable<T> elements, Integer numberOfLast) {
        List<T> lastElements = new ArrayList<>();
        int totalSize = ((Collection<?>) elements).size();
        int count = 0;
        for (T element : elements) {
            if (count + numberOfLast >= totalSize)
                lastElements.add(element);
            count++;
        }

        return lastElements;
    }
    public static <T> T getLastElement(final Iterable<T> elements) {
        T lastElement = null;
        for (T element : elements) {
            lastElement = element;
        }
        return lastElement;
    }

    public static PharmacyDateRange getCurrentMonthRange() {
        Date start, end;
        {
            Calendar calendar = getCalendarForNow();
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            setTimeToBeginningOfDay(calendar);
            start = calendar.getTime();
        }

        {
            Calendar calendar = getCalendarForNow();
            calendar.set(Calendar.DAY_OF_MONTH,
                    calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            setTimeToEndOfDay(calendar);
            end = calendar.getTime();
        }
//        System.out.println("---------------------Get Month Range beginning :" + start);
//        System.out.println("---------------------Get Month Range end :" + end);
        return new PharmacyDateRange(start, end);
    }

    public static PharmacyDateRange getMonthRange(Date date) {
        Date start, end;
        {
            Calendar calendar = getCalendarForDate(date);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            setTimeToBeginningOfDay(calendar);
            start = calendar.getTime();
        }

        {
            Calendar calendar = getCalendarForDate(date);
            calendar.set(Calendar.DAY_OF_MONTH,
                    calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            setTimeToEndOfDay(calendar);
            end = calendar.getTime();
        }
//        System.out.println("---------------------Get Month Range beginning :" + start);
//        System.out.println("---------------------Get Month Range end :" + end);
        return new PharmacyDateRange(start, end);
    }

    public static PharmacyDateRange getDayRange () {
        Date start, end;
        {
            Calendar calendar = getCalendarForNow();
            setTimeToBeginningOfDay(calendar);
            start = calendar.getTime();
        }
        {
            Calendar calendar = getCalendarForNow();
            setTimeToEndOfDay(calendar);
            end = calendar.getTime();
        }

        return new PharmacyDateRange(start, end);
    }

    private static Calendar getCalendarForNow() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        return calendar;
    }

    private static Calendar getCalendarForDate(Date date) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    private static void setTimeToBeginningOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private static void setTimeToEndOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }

    public static Date StringToDate(String dob) throws ParseException {
        //Instantiating the SimpleDateFormat class
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        //Parsing the given String to Date object
        Date date = formatter.parse(dob);
        System.out.println("Date object value: "+date);
        return date;
    }

    public static String dateToDdMmYyyy(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

    public static Integer getConceptIdInGlobalProperties(String property) {
        String value = Context.getAdministrationService().getGlobalProperty("pharmacy.dispensation"+ property + "Concept");
        if (!value.isEmpty()) {
            return Integer.parseInt(value);
        }
        return null;
    }

    public static Concept getConceptInGlobalProperties(String property) {
        String value = Context.getAdministrationService().getGlobalProperty("pharmacy.dispensation"+ property + "Concept");
        if (!value.isEmpty()) {
            Integer conceptId =  Integer.parseInt(value);
            return Context.getConceptService().getConcept(conceptId);
        }
        return null;
    }

    public static Obs getObs(String property, Location location, Patient patient) {
        Obs obs = new Obs();
        obs.setConcept(getConceptInGlobalProperties(property));
        obs.setLocation(location);
        obs.setPerson(patient);
        return obs;
    }

    public static Set<Obs> getDispensationObsList(MobilePatientDispensationInfo dispensationInfo, Patient patient) {
        Set<Obs> obsSet = new HashSet<>();
        Obs obsRegimen = getObs("Regimen", dispensationInfo.getLocation(), patient);
        obsRegimen.setValueCoded(dispensationInfo.getProductRegimen().getConcept());
        obsSet.add(obsRegimen);

        Obs obsGoal = getObs("Goal", dispensationInfo.getLocation(), patient);
        obsGoal.setValueText(dispensationInfo.getGoal().name());
        obsSet.add(obsGoal);

        Obs obsTreatmentDays = getObs("treatmentDays", dispensationInfo.getLocation(), patient);
        obsTreatmentDays.setValueNumeric(dispensationInfo.getTreatmentDays().doubleValue());
        obsSet.add(obsTreatmentDays);

        Obs obsTreatmentEndDate = getObs("treatmentEndDate", dispensationInfo.getLocation(), patient);
        obsTreatmentEndDate.setValueDate(dispensationInfo.getTreatmentEndDate());
        obsSet.add(obsTreatmentEndDate);

        return obsSet;
    }

    public static String generateNumber() {
        String prefix = getUserLocation().getPostalCode();
        DateFormat df = new SimpleDateFormat("yy");
        String formattedDate = df.format(Calendar.getInstance().getTime());
        String returnedNumber;
        do {
            if (prefix != null) {
                String[] prefixSplit = prefix.split("/");
                Generex generex = new Generex(prefixSplit[0] + "-" + prefixSplit[1] + formattedDate + "-[0-9a-z]{4}");
                returnedNumber = generex.random();
            } else {
                Generex generex = new Generex("[0-9]{4}-[a-b0-9]{2}" + formattedDate + "[0-9a-z]{4}");
                returnedNumber = generex.random();
            }
        } while (service().getOneProductOperationByOperationNumber(returnedNumber, Incidence.NEGATIVE) != null);

        return returnedNumber.toUpperCase();
    }

    public static Boolean canDistribute(Location location) {
        return location.getChildLocations() != null && location.getChildLocations().size() != 0;
    }

    public static Boolean isDirectClient(Location location) {
        for (LocationAttribute attribute : location.getActiveAttributes()) {
            if (attribute.getAttributeType().getName().equals("Client Direct NPSP")) {
                if (attribute.getValue().equals(true)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Date getLastMonthOfDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        return cal.getTime();
    }

    public static Date getFirstDateFromStockMax(Location location, Date endDate) {
        String locationType = "CenterAndNGOs";
        if (isDirectClient(location)) {
            locationType = "DirectClient";
        } else if (location.getName().contains("DISTRICT SANITAIRE")) {
            locationType = "District";
        } else {
            if (location.getChildLocations().size() == 0 && !location.getParentLocation().getName().contains("DISTRICT SANITAIRE")) {
                locationType = "PointOfServiceDelivery";
            }
        }
        String value = Context.getAdministrationService().getGlobalProperty("pharmacy.stockMax" + locationType);
        String[] valueSplit = value.split(" ");
        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate);

        switch (valueSplit[1]) {
            case "Months":
                cal.add(Calendar.MONTH, -Integer.parseInt(valueSplit[0]));
                break;
            case "Weeks":
                cal.add(Calendar.DATE, -(Integer.parseInt(valueSplit[0]) * 7));
                break;
            case "Days":
                cal.add(Calendar.DATE, -Integer.parseInt(valueSplit[0]));
                break;
        }
        return cal.getTime();
    }

    public static Double getStockMax(String level) {
        String stockMaxInProperty = Context.getAdministrationService().getGlobalProperty("pharmacy.stockMax" + level);
        String unit = stockMaxInProperty.split(" ")[1];
        double stockMax = 0.0;
        if (unit.startsWith("M"))
            stockMax =  Double.parseDouble(stockMaxInProperty.split(" ")[0]);
        else if (unit.startsWith("D")) {
            stockMax = Double.parseDouble(stockMaxInProperty.split(" ")[0]) / 30;
        } else if (unit.startsWith("W")) {
            stockMax = Double.parseDouble(stockMaxInProperty.split(" ")[0]) / 4;
        }
//        System.out.println("--------------------------------------> Stock Max : " + stockMax);
        return stockMax;
    }

    public static Double getStockMaxDistrictNumber() {
        return getStockMax("District");
    }

    public static Double getStockMaxDirectClientNumber() {
        return getStockMax("DirectClient");
    }

    public static Double getStockMaxCenterAndNGONumber() {
        return getStockMax("CenterAndNGOs");
    }

    public static Double getStockMaxPointOfServiceDeliveryNumber() {
        return getStockMax("PointOfServiceDelivery");
    }

    public static String getEmergencyControlPoint(String level) {
        return Context.getAdministrationService().getGlobalProperty("pharmacy.emergencyControlPoint" + level);
    }

    public static Integer getEmergencyControlPointDistrictNumber() {
        String stockMax = getEmergencyControlPoint("District");
        return Integer.parseInt(stockMax.split(" ")[0]);
    }

    public static String getEmergencyControlPointDistrictUnit() {
        String stockMax = getEmergencyControlPoint("District");
        return stockMax.split(" ")[1];
    }

    public static Integer getEmergencyControlPointDirectClientNumber() {
        String stockMax = getEmergencyControlPoint("DirectClient");
        return Integer.parseInt(stockMax.split(" ")[0]);
    }

    public static Integer getEmergencyControlPointDirectClientUnit() {
        String stockMax = getEmergencyControlPoint("DirectClient");
        return Integer.parseInt(stockMax.split(" ")[1]);
    }

    public static Integer getEmergencyControlPointCenterAndNGONumber() {
        String stockMax = getEmergencyControlPoint("CenterAndNGOs");
        return Integer.parseInt(stockMax.split(" ")[0]);
    }

    public static Integer getEmergencyControlPointCenterAndNGOUnit() {
        String stockMax = getEmergencyControlPoint("CenterAndNGOs");
        return Integer.parseInt(stockMax.split(" ")[1]);
    }

    public static Integer getEmergencyControlPointPointOfServiceDeliveryNumber() {
        String stockMax = getEmergencyControlPoint("PointOfServiceDelivery");
        return Integer.parseInt(stockMax.split(" ")[0]);
    }

    public static Integer getEmergencyControlPointPointOfServiceDeliveryUnit() {
        String stockMax = getEmergencyControlPoint("PointOfServiceDelivery");
        return Integer.parseInt(stockMax.split(" ")[1]);
    }

    public static Double getUserLocationStockMax() {
        return getLocationStockMax(getUserLocation());
    }

    public static Double getLocationStockMax(Location location) {
        if (isDirectClient(location)) {
//            System.out.println("------------------> Is client");
            return getStockMaxDirectClientNumber();
        } else if (location.getName().contains("DISTRICT SANITAIRE")) {
//            System.out.println("------------------> Is District");
            return getStockMaxDistrictNumber();
        } else {
            if (location.getChildLocations().size() == 0 && !location.getParentLocation().getName().contains("DISTRICT SANITAIRE")) {
//                System.out.println("------------------> Is PPS");
                return getStockMaxPointOfServiceDeliveryNumber();
            }
        }
//        System.out.println("------------------> Is Center");
        return getStockMaxCenterAndNGONumber();
    }

    public static Integer getMonthsForCMM() {
        String months = Context.getAdministrationService().getGlobalProperty("pharmacy.monthsForCMM");
        return Integer.parseInt(months);
    }

    public static Map<Integer, String> getServices() {
        Map<Integer, String> services = new HashMap<>();
        services.put(0,"Cancérologie");
        services.put(1,"CDI");
        services.put(2,"CDT");
        services.put(3,"Chirurgie générale et digestive");
        services.put(4,"Chirurgie pédiatrique");
        services.put(5,"Consultations externes");
        services.put(6,"CPN");
        services.put(7,"Dermatologie");
        services.put(8,"Diabétologie");
        services.put(9,"Dispensaire");
        services.put(10,"Gastro-entérologie");
        services.put(11,"Gynécologie-obstétrique ");
        services.put(12,"Hématologie");
        services.put(13,"Hospitalisation");
        services.put(14,"Laboratoire");
        services.put(15,"Maternité");
        services.put(16,"Médecine générale");
        services.put(17,"Néonatalogie");
        services.put(18,"Néphrologie");
        services.put(19,"Neurologie ");
        services.put(20,"Nutrition");
        services.put(21,"O.R.L");
        services.put(22,"Odonto-stomatologique");
        services.put(23,"Ophtalmologie");
        services.put(24,"Pédiatrie");
        services.put(25,"Pneumologie (PPH)");
        services.put(26,"Post-natal");
        services.put(27,"Réanimation");
        services.put(28,"Rééducation et réadaptation fonctionnelles");
        services.put(29,"Rhumatologie");
        services.put(30,"SMIT");
        services.put(31,"Traumatologie et chirurgie orthopédique ");
        services.put(32,"Urgences");
        services.put(33,"Urologie");
        return services;
    }

    public static ProductAttributeOtherFlux createProductAttributeOtherFlux(Product product, Double quantity, String label, ProductReport report) {
        ProductAttributeOtherFlux productAttributeOtherFlux = fluxService().getOneProductAttributeOtherFluxByProductAndOperationAndLabel(
                product,
                report,
                label,
                report.getLocation()
        );
        if (productAttributeOtherFlux != null){
            productAttributeOtherFlux.setQuantity(quantity);
        } else {
            productAttributeOtherFlux = getProductAttributeOtherFlux(product, quantity, label, report, reportService());
            productAttributeOtherFlux.setProductOperation(report);
        }
        return  productAttributeOtherFlux;
    }

    public static ProductAttributeOtherFlux getProductAttributeOtherFlux(Product product, Double quantity, String label, ProductReport report, ProductReportService productReportService) {
        ProductAttributeOtherFlux productAttributeOtherFlux;
        productAttributeOtherFlux = new ProductAttributeOtherFlux();
        productAttributeOtherFlux.setQuantity(quantity);
        switch (label) {
            case "SI": {
                ProductAttributeOtherFlux lastOtherFlux = productReportService.getPreviousReportProductAttributeOtherFluxByLabel(product, "SDU", report, report.getLocation());
                if (lastOtherFlux != null) {
                    productAttributeOtherFlux.setQuantity(lastOtherFlux.getQuantity());
                }
                break;
            }
            case "DM1": {
                ProductAttributeOtherFlux lastOtherFlux = productReportService.getPreviousReportProductAttributeOtherFluxByLabel(product, "QD", report, report.getLocation());
                if (lastOtherFlux != null) {
                    productAttributeOtherFlux.setQuantity(lastOtherFlux.getQuantity());
                }
                break;
            }
            case "DM2": {
                ProductAttributeOtherFlux lastOtherFlux = productReportService.getPreviousReportProductAttributeOtherFluxByLabel(product, "DM1", report, report.getLocation());
                if (lastOtherFlux != null) {
                    productAttributeOtherFlux.setQuantity(lastOtherFlux.getQuantity());
                }
                break;
            }

        }
        productAttributeOtherFlux.setLabel(label);
        productAttributeOtherFlux.setLocation(report.getLocation());
        productAttributeOtherFlux.setProduct(product);
        return productAttributeOtherFlux;
    }

    public static List<ProductAttributeFlux> createProductAttributeFluxes(Product product, ProductOperation operation, Integer quantity) {
        List<ProductAttributeFlux> fluxes = new ArrayList<>();
        List<ProductAttributeStock> productAttributeStocks = stockService().getAllProductAttributeStockByProduct(product, operation.getLocation());
        int countFlux = 0;
        int countOldFlux = operation.getProductAttributeFluxes().size();
        for (ProductAttributeStock stock : productAttributeStocks) {
            if (stock.getQuantityInStock() == 0) {
                continue;
            }
            countFlux ++;
            ProductAttributeFlux productAttributeFlux = fluxService().getOneProductAttributeFluxByAttributeAndOperation(
                    stock.getProductAttribute(),
                    operation
            );
            if (productAttributeFlux == null) {
                productAttributeFlux = new ProductAttributeFlux();
                productAttributeFlux.setLocation(OperationUtils.getUserLocation());
                productAttributeFlux.setProductAttribute(stock.getProductAttribute());
                productAttributeFlux.setOperationDate(operation.getOperationDate());
                productAttributeFlux.setProductOperation(operation);
            }

            if (quantity <= stock.getQuantityInStock()){
                productAttributeFlux.setQuantity(quantity);
                fluxes.add(productAttributeFlux);
                break;
            } else {
                productAttributeFlux.setQuantity(stock.getQuantityInStock());
                quantity -= stock.getQuantityInStock();
                fluxes.add(productAttributeFlux);
                if (quantity.equals(0)) {
                    break;
                }
            }
        }
        if (countOldFlux > countFlux) {
            int remainFluxesCount = countOldFlux - countFlux;
            List<ProductAttributeFlux> attributeFluxes =
                    OperationUtils.getLastElements(
                            operation.getProductAttributeFluxes(),
                            remainFluxesCount
                    );
            for (ProductAttributeFlux flux : attributeFluxes) {
                flux.setQuantity(0);
                fluxes.add(flux);
            }
        }
        return fluxes;
    }
}
