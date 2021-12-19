package pias.backend.id.server.database;

import pias.backend.id.server.entity.PIA;
import pias.backend.id.server.entity.PIACreate;
import pias.backend.id.server.entity.PIAUpdate;

public interface PIAService {
  PIA create(PIACreate piaCreate);

  PIA read(long id, long version);

  PIA read(long id);

  PIA update(PIAUpdate piaUpdate);
}
