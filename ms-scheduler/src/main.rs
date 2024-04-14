use std::time::Duration;

#[tokio::main]
async fn main(){
    println!("Hello, world!");

    for i in 0..100000 {
        tokio::spawn(async move{
            tokio::time::sleep(Duration::from_secs(10 + i)).await;
            println!("I am the task number {}", i)
        });
    }

    tokio::time::sleep(Duration::from_secs(1000000)).await
}
