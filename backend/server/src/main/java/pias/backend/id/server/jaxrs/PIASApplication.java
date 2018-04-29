package pias.backend.id.server.jaxrs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.eclipse.collections.impl.factory.Sets;
import pias.backend.id.server.avro.PIAAvprImpl;
import pias.backend.id.server.database.CustomerProfileService;
import pias.backend.id.server.database.PIAService;
import pias.backend.id.server.database.SubjectProfileService;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

@ApplicationPath("rest")
public class PIASApplication extends Application {
    private final CustomerProfileService mysqlCustomerProfileService;
    private final SubjectProfileService mysqlSubjectProfileService;
    private final PIAService mysqlPiaService;
    private final PIAAvprImpl piaAvprImpl;

    public PIASApplication(CustomerProfileService mysqlCustomerProfileService, SubjectProfileService mysqlSubjectProfileService, PIAService mysqlPiaService, PIAAvprImpl piaAvprImpl) {
        this.mysqlCustomerProfileService = mysqlCustomerProfileService;
        this.mysqlSubjectProfileService = mysqlSubjectProfileService;
        this.mysqlPiaService = mysqlPiaService;
        this.piaAvprImpl = piaAvprImpl;
    }

    public Set<Object> getSingletons() {
        ObjectMapper objectMapper = new ObjectMapper();
        JacksonJsonProvider jacksonJsonProvider = new JacksonJsonProvider(objectMapper);
        CustomerProfileServiceJaxRs customerProfileJaxrs = new CustomerProfileServiceJaxRs(mysqlCustomerProfileService);
        SubectProfileServiceJaxRs subectProfileServiceJaxRs = new SubectProfileServiceJaxRs(mysqlSubjectProfileService);
        PIAServiceJaxRs piasAnnexOneServiceJaxRs = new PIAServiceJaxRs(mysqlPiaService, piaAvprImpl);
        return Sets.immutable.<Object>of(customerProfileJaxrs, subectProfileServiceJaxRs, piasAnnexOneServiceJaxRs, jacksonJsonProvider).castToSet();
    }
}
