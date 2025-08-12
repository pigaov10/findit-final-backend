package com.imogo.imogo_backend.seeder;

import com.imogo.imogo_backend.model.*;
import com.imogo.imogo_backend.model.enums.ImobPropertyType;
import com.imogo.imogo_backend.model.enums.ImobStatus;
import com.imogo.imogo_backend.model.enums.VisitStatus;
import com.imogo.imogo_backend.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class DataSeeder implements CommandLineRunner {

    private final AgencyRepository agencyRepository;
    private final AgentRepository agentRepository;
    private final PropertyRepository propertyRepository;
    private final VisitRepository visitRepository;

    public DataSeeder(
            AgencyRepository agencyRepository,
            AgentRepository agentRepository,
            PropertyRepository propertyRepository,
            VisitRepository visitRepository
    ) {
        this.agencyRepository = agencyRepository;
        this.agentRepository = agentRepository;
        this.propertyRepository = propertyRepository;
        this.visitRepository = visitRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!agencyRepository.findAll().isEmpty()) {
            System.out.println("Agências já existem. Seed ignorado.");
            return;
        }

        // === 1. Criar 10 Agências ===
        List<Agency> agencies = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Agency agency = new Agency();
            agency.setName("Agência " + i);
            agency.setLicenseNumber("LIC" + (120 + i));
            agency.setEmail("agencia" + i + "@email.com");
            agency.setPhone("1199999000" + i);
            agency.setTaxId("1234567890" + i);
            agency.setAddress("Rua Exemplo, " + (100 + i));
            agency.setCity("Cidade " + i);
            agency.setDistrict("Bairro " + i);
            agency.setPostalCode("0000" + i);
            agency.setWebsite("www.agencia" + i + ".com");
            agency.setContactPerson("Contato " + i);
            agency.setNotes("Nota para agência " + i);
            agency.setStatus("active");
            agencies.add(agency);
        }
        agencyRepository.saveAll(agencies);

        // === 2. Criar 10 Agentes vinculados às agências ===
        List<Agent> agents = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Agent agent = new Agent();
            agent.setName("Agente " + i);
            agent.setEmail("agente" + i + "@email.com");
            agent.setPhone("1198888000" + i);
            agent.setLicenseNumber("AGT" + (100 + i));
            agent.setTaxId("9876543210" + i);
            agent.setAddress("Rua Agente " + i);
            agent.setCity("Cidade " + i);
            agent.setDistrict("Bairro Agente " + i);
            agent.setPostalCode("9999" + i);
            agent.setWebsite("www.agente" + i + ".com");
            agent.setContactPerson("Pessoa Contato " + i);
            agent.setNotes("Notas do agente " + i);
            agent.setStatus("active");
            agent.setAgency(agencies.get((i - 1) % agencies.size())); // distribuindo entre agências
            agents.add(agent);
        }
        agentRepository.saveAll(agents);

        // === 3. Criar 10 Imóveis vinculados aos agentes ===
        for (int i = 1; i <= 10; i++) {
            ImobPropertyLocation location = new ImobPropertyLocation();
            location.setLatitude(-23.5 + i * 0.01);
            location.setLongitude(-46.6 + i * 0.01);
            location.setCity("Cidade " + i);
            location.setState("Estado " + i);
            location.setCountry("Brasil");
            location.setAddress("Rua Imóvel " + i);
            location.setZipcode("11000-00" + i);

            ImobProperty property = new ImobProperty();
            property.setTitle("Imóvel Exemplo " + i);
            property.setDescription("Descrição detalhada do imóvel " + i);
            property.setPrice(new BigDecimal(100000 + (i * 50000)));
            property.setPurpose("Venda");
            property.setCity("Cidade " + i);
            property.setBathrooms(1 + i % 3);
            property.setBedrooms(1 + i % 4);
            property.setSquareFeet(50 + i * 10);
            property.setStatus(ImobStatus.PENDING);
            property.setType(ImobPropertyType.HOUSE);
            property.setAgent(agents.get((i - 1) % agents.size()));
            property.setPropertyLocation(location);

            // === 4. Criar imagem fake (pode carregar imagem real de arquivo também) ===
            byte[] imageData = ("Imagem falsa " + i).getBytes(); // apenas exemplo

            ImobPropertyImage image = new ImobPropertyImage();
            image.setImageData(imageData);
            image.setImobProperty(property);

            property.setPropertyImages(Collections.singletonList(image));

            propertyRepository.save(property);

            List<ImobProperty> allProperties = propertyRepository.findAll();
            List<Agent> allAgents = agentRepository.findAll();

            for (int j = 0; j < 10; j++) {
                Visit visit = new Visit();
                visit.setAgent(allAgents.get(i % allAgents.size()));
                visit.setImobProperty(allProperties.get(i % allProperties.size()));
                visit.setVisitDatetime(LocalDateTime.now().plusDays(i));
                visit.setDurationMinutes(60);
                visit.setClientName("Cliente " + j);
                visit.setTitle("Visita no Piscine " +s j);
                visit.setClientPhone("1199000000" + j);
                visit.setClientEmail("cliente" + j + "@email.com");
                visit.setNotes("Visita agendada pelo sistema.");
                visit.setStatus(VisitStatus.SCHEDULED);
                visit.setConfirmedByClient(j % 2 == 0);
                visit.setConfirmedByAgent(j % 3 == 0);

                visitRepository.save(visit);
            }
        }

        System.out.println("Seed de dados completo: 10 agências, 10 agentes, 10 imóveis.");
    }
}
