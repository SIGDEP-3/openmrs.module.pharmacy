package org.openmrs.module.pharmacy.utils;

import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductAttributeFlux;
import org.openmrs.module.pharmacy.ProductAttributeStock;
import org.openmrs.module.pharmacy.ProductOperation;
import org.openmrs.module.pharmacy.ProductProgram;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.api.ProductAttributeFluxService;
import org.openmrs.module.pharmacy.api.ProductAttributeStockService;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;
import org.openmrs.module.pharmacy.models.PharmacyDateRange;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class OperationUtils {
    public static Boolean validateOperation(ProductOperation operation) {

        if (!operation.getIncidence().equals(Incidence.NONE)) {
            if (!operation.getOperationStatus().equals(OperationStatus.VALIDATED)) {
                Set<ProductAttributeFlux> fluxes = operation.getProductAttributeFluxes();
                if (fluxes != null && fluxes.size() != 0) {
                    for (ProductAttributeFlux flux : fluxes) {
                        ProductAttributeStock attributeStock = stockService().getOneProductAttributeStockByAttribute(flux.getProductAttribute(), getUserLocation(), false);
                        if (attributeStock != null) {
                            Integer quantity = operation.getIncidence().equals(Incidence.POSITIVE) ?
                                    attributeStock.getQuantityInStock() + flux.getQuantity() :
                                    (operation.getIncidence().equals(Incidence.NEGATIVE) ? attributeStock.getQuantityInStock() - flux.getQuantity() : flux.getQuantity());
                            attributeStock.setQuantityInStock(quantity);
                        } else {
                            attributeStock = new ProductAttributeStock();
                            attributeStock.setQuantityInStock(flux.getQuantity());
                            attributeStock.setLocation(getUserLocation());
                            attributeStock.setProductAttribute(flux.getProductAttribute());
                        }
                        stockService().saveProductAttributeStock(attributeStock);

                        flux.setStatus(OperationStatus.VALIDATED);
                        fluxService().saveProductAttributeFlux(flux);
                    }
                }
                operation.setOperationStatus(OperationStatus.VALIDATED);
                service().saveProductOperation(operation);

                return true;
            }
        }

        return false;
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
        if (!operation.getIncidence().equals(Incidence.NONE)) {
            if (operation.getOperationStatus().equals(OperationStatus.VALIDATED)) {
                Set<ProductAttributeFlux> fluxes = operation.getProductAttributeFluxes();
                if (fluxes != null && fluxes.size() != 0) {
                    for (ProductAttributeFlux flux : fluxes) {
                        if (flux.getStatus().equals(OperationStatus.VALIDATED)) {
                            ProductAttributeStock attributeStock = stockService().getOneProductAttributeStockByAttribute(flux.getProductAttribute(), getUserLocation(), false);
                            if (attributeStock != null) {
                                Integer quantity = operation.getIncidence().equals(Incidence.POSITIVE) ?
                                        attributeStock.getQuantityInStock() - flux.getQuantity() :
                                        (operation.getIncidence().equals(Incidence.NEGATIVE) ? attributeStock.getQuantityInStock() + flux.getQuantity() : flux.getQuantity());
                                attributeStock.setQuantityInStock(quantity);
                            }
                            stockService().saveProductAttributeStock(attributeStock);

                            flux.setStatus(OperationStatus.DISABLED);
                            fluxService().saveProductAttributeFlux(flux);
                        }
                    }
                }
                operation.setOperationStatus(OperationStatus.DISABLED);
                service().saveProductOperation(operation);

                return true;
            }
        }
        return false;
    }

    private static PharmacyService service() {
        return Context.getService(PharmacyService.class);
    }

    private static ProductAttributeFluxService fluxService() {
        return Context.getService(ProductAttributeFluxService.class);
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

    public static PharmacyDateRange getMonthRange() {
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

//        System.out.println("---------------------Get Day Range beginning :" + start);
//        System.out.println("---------------------Get Day Range end :" + end);
        return new PharmacyDateRange(start, end);
    }

    private static Calendar getCalendarForNow() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
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

}
