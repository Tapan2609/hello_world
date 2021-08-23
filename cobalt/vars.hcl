{{ $app_name := "sampleapp-java" }}
{{ $product_path := "ariba-sampleapp-java" }}
[[ file "default.vars.hcl" ]]

{{ $greeting_cpu := "500" }}
{{ $greeting_memory := "512" }}

{{ $nginx_memory := "200" }}
{{ $nginx_cpu := "500" }}

{{ $int_cpu := "500" }}
{{ $int_memory := "512" }}

{{ $test_cpu := "500" }}
{{ $test_memory := "512" }}

{{ $nfs_cpu := "200" }}
{{ $nfs_memory := "512" }}

{{ $ftp_memory := "200" }}
{{ $ftp_cpu := "500" }}
{{ $enable_code_coverage := true }}