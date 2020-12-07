package org.openmrs.module.pharmacy.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.Product;
import org.openmrs.module.pharmacy.ProductProgram;
import org.openmrs.module.pharmacy.api.ProductProgramService;
import org.openmrs.module.pharmacy.api.ProductService;
import org.openmrs.module.pharmacy.api.ProductUnitService;
import org.openmrs.module.pharmacy.models.ProductUploadResumeDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CSVHelper {
    //    public static String TYPE = "text/csv";
    public static String TYPE = "application/vnd.ms-excel";
    static String[] HEADERs = { "#Code", "#Designation", "#Designation de dispensation", "#Unite de conditionnement",
            "#Nombre unite", "#Unite de dispensation", "#Prix de vente", "#Programme"};

    public static boolean hasCSVFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public static ProductUploadResumeDTO csvImportProducts(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim().withDelimiter(';'))) {

            List<Product> products = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            ProductUploadResumeDTO resumeDTO = new ProductUploadResumeDTO();
            resumeDTO.setTotalToImport(((Collection<?>)csvRecords).size());

            int count = 0;
            for (CSVRecord csvRecord : csvRecords) {
                boolean hasChanged = false;
                ProductProgram program = programService().getOneProductProgramByName(csvRecord.get("#Programme"));
                Product product = productService().getOneProductByCode(csvRecord.get("#Code"));
                if (product == null) {
                    product = getNewProduct(csvRecord);
//                    //product = containsCode(products, csvRecord.get("#Code"));
//                    if (product == null) {
//                    }
                    resumeDTO.addOneNewImported();
                    product.addProgram(program);
                    hasChanged = true;
                    // products.add(product);
                    productService().saveProduct(product);
                } else {
                    if (!product.getProductPrograms().contains(program)) {
                        product.addProgram(program);
                        hasChanged = true;
                    }
                    resumeDTO.addOneUpdateImported();
                }
                if (hasChanged) {
                    resumeDTO.addOneTotalImported();
                }
                System.out.println("--------- Product : " + ++count);
            }
            for (CSVRecord csvRecord : csvRecords) {
                Product product = productService().getOneProductByCode(csvRecord.get("#Code"));
                ProductProgram program = programService().getOneProductProgramByName(csvRecord.get("#Programme"));
                System.out.println("--------- Product : " + ++count);
                if (program != null) {
                    if (product.getProductPrograms().contains(program))
                        continue;
                    product.addProgram(program);
                    productService().saveProduct(product);
                }
            }

            return resumeDTO;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

    public static List<Product> csvProducts(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim().withDelimiter(';'))) {

            List<Product> products = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            String message = "";

            int count = 0;
            for (CSVRecord csvRecord : csvRecords) {
                ProductProgram program = programService().getOneProductProgramByName(csvRecord.get("#Programme"));
                Product product = containsCode(products, csvRecord.get("#Code"));
                if (product != null) {
                    products.remove(product);
                    product.addProgram(program);
                    products.add(product);
                    message = "added program to existing in list : " + csvRecord.get("#Code");
                } else {
                    product = productService().getOneProductByCode(csvRecord.get("#Code"));
                    if (product == null) {
                        product = getNewProduct(csvRecord);
                        product.addProgram(program);
                        products.add(product);
                        message = "added program to new in all : " + csvRecord.get("#Code");
                    } else {
                        if (!product.getProductPrograms().contains(program)) {
                            product.addProgram(program);
                            products.add(product);
                            message = "added program to existing in db : " + csvRecord.get("#Code");
                        }
                    }
                }
                System.out.println("--------- Product [" + message + "]: " + ++count);
            }

            return products;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

    private static Product getNewProduct(CSVRecord csvRecord) {
        Product product = new Product();
        product.setCode(csvRecord.get("#Code"));
        product.setRetailName(csvRecord.get("#Designation de dispensation"));
        product.setWholesaleName(csvRecord.get("#Designation"));
        product.setProductRetailUnit(unitService().getOneProductUnitByName(csvRecord.get("#Unite de dispensation")));
        product.setProductWholesaleUnit(unitService().getOneProductUnitByName(csvRecord.get("#Unite de conditionnement")));
        product.setUnitConversion(Double.parseDouble(csvRecord.get("#Nombre unite")));
        product.setUuid(csvRecord.get("#Code") + "PPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
        return product;
    }

    private static Product containsCode(List<Product> products, String code) {
        for (Product product: products) {
            if (product.getCode().equals(code)){
                return product;
            }
        }
        return null;
    }

    private static ProductService productService() {
        return Context.getService(ProductService.class);
    }

    private static ProductUnitService unitService() {
        return Context.getService(ProductUnitService.class);
    }

    private static ProductProgramService programService() {
        return Context.getService(ProductProgramService.class);
    }

}
