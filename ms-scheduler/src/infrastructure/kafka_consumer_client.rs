use std::env;
use kafka::consumer::{Consumer, FetchOffset, GroupOffsetStorage};

pub struct KafkaConsumerClient {
    running: bool,
    bootstrap_servers: String,
    group_id: String,
}

impl KafkaConsumerClient {

    pub async fn start(&mut self) {
        self.running = true;

        println!("Bootstrap server: {:?}", self.bootstrap_servers);
        println!("Group ID: {:?}", self.group_id);

        let mut consumer = Consumer::from_hosts(vec![self.bootstrap_servers.clone()])
        // let mut consumer = Consumer::from_hosts(vec!["kafka:9092".to_string()])
            .with_topic("new-event".to_string())
            .with_group(self.group_id.clone())
            .with_fallback_offset(FetchOffset::Earliest)
            .with_offset_storage(Some(GroupOffsetStorage::Kafka))
            .create()
            .unwrap();

        println!("Consumer started");

        loop {
            if self.running {
                let message = consumer.poll().unwrap();
                if !message.is_empty() {
                    for ms in message.iter() {
                        for m in ms.messages() {
                            println!(
                                "{}:{}@{}: {:?}",
                                ms.topic(),
                                ms.partition(),
                                m.offset,
                                std::str::from_utf8(&m.value)
                            );
                        }
                        let _ = consumer.consume_messageset(ms);
                    }

                    let _ = consumer.commit_consumed().unwrap();
                }
            }
        }

    }

    pub fn stop(&mut self) {
        self.running = false
    }

}

impl Default for KafkaConsumerClient {
    fn default() -> Self {
        let bootstrap_servers = env::var("rust.kafka.bootstrap-servers")
            .unwrap_or_else(|_| { "localhost:9092".to_string() });
        let group_id = env::var("rust.kafka.consumer.group-id")
            .unwrap_or_else(|_| { "default_group".to_string() } );

        return KafkaConsumerClient { running: false, bootstrap_servers, group_id }
    }
}

