package be.syntra.devshop.DevshopFront.init;

import be.syntra.devshop.DevshopFront.repositories.DataFillerService;
import be.syntra.devshop.DevshopFront.services.DataFillerImpl;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(propagation = Propagation.REQUIRED)
@NoArgsConstructor
public class DataFillerExecutor implements DataFillerService, ApplicationRunner {

    private DataFillerImpl dataFiller;

    @Autowired
    public DataFillerExecutor(DataFillerImpl dataFiller) {
        this.dataFiller = dataFiller;
    }

    @Override
    public void importData() {
        dataFiller.initialize();
    }

    @Override
    public void run(ApplicationArguments args) {
        importData();
    }
}


