package pias.avro.cli;

import com.beust.jcommander.JCommander;
import org.apache.avro.AvroRemoteException;
import org.apache.avro.Protocol;
import org.apache.avro.ipc.HttpTransceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;

import pias.backend.PIAAvpr;
import pias.backend.avro.CustomerProfileAvpr;
import pias.backend.avro.CustomerProfileAvro;
import pias.backend.avro.SubjectProfileAvpr;

import java.io.IOException;
import java.net.URL;

public class App {
    static CustomerProfileAvpr customerProfileAvpr = getAvprClient(CustomerProfileAvpr.class, CustomerProfileAvpr.PROTOCOL);
    static SubjectProfileAvpr subjectProfileAvpr = getAvprClient(SubjectProfileAvpr.class, SubjectProfileAvpr.PROTOCOL);
    static PIAAvpr privacyImpactAssessmentAnnexOneAvpr = getAvprClient(PIAAvpr.class, PIAAvpr.PROTOCOL);

    public static void main(String args[]) throws IOException {
        final CreateCustomerProfile createCustomerProfile = new CreateCustomerProfile();
        final ReadCustomerProfile readCustomerProfile = new ReadCustomerProfile();
        final CreateSubjectProfile createSubjectProfile = new CreateSubjectProfile();
        final String createCustomer = "create-customer",
                createSubject = "create-subject",
                readCustomer = "read-customer";
        JCommander jc = JCommander.newBuilder()
                .addCommand(createCustomer, createCustomerProfile)
                .addCommand(readCustomer, readCustomerProfile)
                .addCommand(createSubject, createSubjectProfile)
                .build();
        jc.parse(args);
        final String parsedCommand = jc.getParsedCommand();
        switch (parsedCommand) {
            case createCustomer:
                createCustomerProfile(createCustomerProfile);
                break;
            case readCustomer:
                readCustomerProfile(readCustomerProfile);
                break;
            case createSubject:
                createSubjectProfile(createSubjectProfile);
                break;
            default:
                throw new IllegalStateException("Unknown command");
        }
    }

    private static void createSubjectProfile(CreateSubjectProfile createSubjectProfile) {
        try {
            System.out.println(subjectProfileAvpr.avroCreateSubjectProfile(createSubjectProfile.getSubjectProfileCreateAvro()));
        } catch (AvroRemoteException e) {
            throw new IllegalStateException(e);
        }

    }


    private static void readCustomerProfile(ReadCustomerProfile readCustomerProfile) {
        CustomerProfileAvro customerProfileAvro = readCustomerProfile.getVersion().map(v -> {
            try {
                return App.customerProfileAvpr.avroReadVersionedCustomerProfile(readCustomerProfile.getId(), v);
            } catch (AvroRemoteException e) {
                throw new RuntimeException(e);
            }
        }).orElseGet(() -> {
            try {
                return App.customerProfileAvpr.avroReadCustomerProfile(readCustomerProfile.getId());
            } catch (AvroRemoteException e) {
                throw new RuntimeException(e);
            }
        });
        System.out.println(customerProfileAvro);
    }

    private static void createCustomerProfile(CreateCustomerProfile createCustomerProfile) throws IOException {
        CustomerProfileAvro customerProfileAvro = App.customerProfileAvpr.avroCreateCustomerProfile(createCustomerProfile.getCustomerProfileCreateAvro());
        System.out.println(customerProfileAvro);
    }

    private static <T> T getAvprClient(Class<T> iface, Protocol protocol) {
        try {
            HttpTransceiver nettyTransceiver = new HttpTransceiver(new URL(String.format("http://127.0.0.1:8000/avpr/%s.%s", protocol.getNamespace(), protocol.getName())));
            return SpecificRequestor.getClient(iface, nettyTransceiver);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
