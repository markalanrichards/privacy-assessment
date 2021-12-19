package pias.backend.id.server.jaxrs;

import pias.backend.id.server.database.SubjectProfileService;
import pias.backend.id.server.entity.SubjectProfile;
import pias.backend.id.server.entity.SubjectProfileCreate;
import pias.backend.id.server.entity.SubjectProfileUpdate;
import pias.backend.id.server.jaxrs.model.SubjectProfileCreateJaxRs;
import pias.backend.id.server.jaxrs.model.SubjectProfileJaxRs;
import pias.backend.id.server.jaxrs.model.SubjectProfileUpdateJaxRs;

public class SubectProfileServiceJaxRs implements SubjectProfileServiceInterfaceJaxRs {

  private final SubjectProfileService mysqlSubjectProfileService;

  public SubectProfileServiceJaxRs(SubjectProfileService mysqlSubjectProfileService) {
    this.mysqlSubjectProfileService = mysqlSubjectProfileService;
  }

  public SubjectProfileJaxRs create(SubjectProfileCreateJaxRs customerProfileCreateJaxRs) {
    SubjectProfileCreate customerProfileCreate = customerProfileCreateJaxRs.fromJaxRs();
    SubjectProfile customerProfile =
        mysqlSubjectProfileService.createSubjectProfile(customerProfileCreate);
    return SubjectProfileJaxRs.toJaxRs(customerProfile);
  }

  @Override
  public SubjectProfileJaxRs update(SubjectProfileUpdateJaxRs customerProfileUpdateJaxRs) {
    SubjectProfileUpdate customerProfileUpdate = customerProfileUpdateJaxRs.fromJaxRs();
    SubjectProfile customerProfile =
        mysqlSubjectProfileService.updateSubjectProfile(customerProfileUpdate);
    return SubjectProfileJaxRs.toJaxRs(customerProfile);
  }

  @Override
  public SubjectProfileJaxRs read(String id) {
    SubjectProfile customerProfile =
        mysqlSubjectProfileService.readSubjectProfile(Long.valueOf(id));
    return SubjectProfileJaxRs.toJaxRs(customerProfile);
  }

  @Override
  public SubjectProfileJaxRs read(String id, String version) {
    SubjectProfile customerProfile =
        mysqlSubjectProfileService.readSubjectProfile(Long.valueOf(id), Long.valueOf(version));
    return SubjectProfileJaxRs.toJaxRs(customerProfile);
  }
}
