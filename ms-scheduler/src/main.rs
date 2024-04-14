use std::time::Duration;

#[tokio::main]
async fn main(){
    println!("Hello, world!");

    for mut i in 0..100000 {
        tokio::spawn(async move {
            let mut mutable_index = i;
            if i > 100 && i < 200 {
                mutable_index = 0
            }
            tokio::time::sleep(Duration::from_secs(10 + mutable_index)).await;
            println!("I am the task number {}", i)
        });
    }

    tokio::time::sleep(Duration::from_secs(1000000)).await
}
