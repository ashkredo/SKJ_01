# Network of Counters (SKJ)

Tworzenie agentów:

Odbywa się za pomocą stworzenia nowych obiektów klasy Agent. 

Wargumentach konstruktora podajemy IP , port , wartość początkową swojego licznika oraz adres agenta wprowadzającego (jeżeli nie ma takiego agenta, to podajemy null).
  
Przykład :

Agent agent1 = new Agent("localhost", 9991, new Counter(new AtomicLong(0)), null);

Agent agent2 = new Agent("localhost", 9992, new Counter(new AtomicLong(0)), new AgentAddress(agent1.IP, agent1.port));

Opis komunikacji:

Komunikacja między agentami odbyła się za pomocą abstraktnej klasy AgentCommunication którą dziedziczy Agent.

Licznik:

Klasa Counter reprezentuje licznik. Wartością licznika jest zmienna value. 

Zamiast zwykłego long użyłem AtomicLong żeby uniknąć problemów z wątkami.

http://localhost:10000/app
