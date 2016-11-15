package de.effectivetrainings.tracer;

import de.effectivetrainings.TracerUiApplication;
import de.effectivetrainings.tracer.domain.ServiceCall;
import de.effectivetrainings.tracer.domain.Span;
import de.effectivetrainings.tracer.domain.TimeSpan;
import de.effectivetrainings.tracer.repository.TracerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TracerUiApplication.class)
@RunWith(SpringRunner.class)
public class TracerApplicationTest {

    @Autowired
    private TracerRepository tracerRepository;

    @Test
    public void run() {
        final List<Span> spans = tracerRepository.findSpans();
        System.out.println(spans);
    }

    @Test
    public void findCalls() {
        final Set<ServiceCall> calls = tracerRepository.findCalls();
        System.out.println(calls);
    }

    @Test
    public void findPeriod() {
        final Optional<TimeSpan> period = tracerRepository.findTimespan();
        System.out.println(period.get());
    }
}
